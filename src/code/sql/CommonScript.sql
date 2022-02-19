-- 按照商品分类分组排序查商品数量前三的数据 不足三个有多少个展示多少个
-- 表结构 product.id 商品ID product.category 商品分类 product.amount 商品数量
SELECT a.* from product a
WHERE 3 > (SELECT COUNT(*) from product b WHERE b.amount > a.amount and a.category = b.category )
ORDER BY a.category,a.amount DESC;