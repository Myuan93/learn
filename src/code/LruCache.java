package code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhouk
 * @date: 2022/2/14 8:53 下午
 * @description:LRU缓存实现 Least Recently Used 优先淘汰最近很少使用的数据
 */
public class LruCache {

    public Entry head;

    public Entry tail;

    private int capacity;

    private Map<Integer, Entry> cache;

    private int size;

    public LruCache(int capacity) {
        head = new Entry();
        tail = new Entry();
        head.next = tail;
        tail.pre = head;
        this.capacity = capacity;
        cache = new HashMap<>(capacity + 2);
        this.size = 0;
    }

    public void put(int key, int value) {
        Entry node = cache.get(key);
        //首先判断该缓存是否存在 存在则移动至头节点
        if (null != node) {
            node.value = value;
            moveToHead(node);
            return;
        }
        //判断当前容量是否已经满了 满了则删除尾节点之前的数据
        if (size == capacity) {
            Entry lastNode = tail.pre;
            delNode(lastNode);
            cache.remove(lastNode.key);
            size--;
        }
        //正常添加数据至缓存 添加到头部
        Entry entry = new Entry(key, value);
        addNode(entry);
        cache.put(key,entry);
        size++;
    }

    private void moveToHead(Entry node){
        //首先删除node的关系链条
        delNode(node);
        //再放在头部去
        addNode(node);
    }

    /**
     * 删除node的关系链条
     * @param node
     */
    private void delNode(Entry node){
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    /**
     * 将node添加到头节点的下一个节点
     * @param node
     */
    private void addNode(Entry node){
        node.next = head.next;
        head.next.pre = node;
        head.next = node;
        node.pre = head;
    }

    public int get(int key) {
        //判断当前key是否存在，不存在返回-1
        if (null == cache.get(key)) {
            return -1;
        }
        //存在则添加到头部
        Entry node = cache.get(key);
        moveToHead(node);
        return node.value;
    }

    public static class Entry {
        public Entry pre;
        public Entry next;
        public int key;
        public int value;

        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public Entry() {

        }
    }

    public static void main(String[] args) {
        LruCache lruCache = new LruCache(2);
        lruCache.put(1,1);
        lruCache.put(2,2);
        System.out.println(lruCache.get(1));
        lruCache.put(3,3);
        System.out.println(lruCache.get(2));
    }

}
