package code.knapsackProblem;

/**
 * @author: zhouk
 * @date: 2022/2/19 1:34 下午
 * @description:背包问题
 */
public class KnapsackProblem {

    /**
     * 背包容量
     */
    private int capacity;

    /**
     * 一个箱子最多能装 20kg的重量。
     * 现有物品如下：
     * 物品a 3kg 价值 4元
     * 物品b 4kg 价值 6元
     * 物品c 2kg 价值  3元
     * 物品d 5kg 价值 8元
     * 物品e 8kg 价值 13元
     * 物品f 7kg  价值 12元
     * 物品g 5kg  价值 11元
     * 通过建模能够输出这个箱子能装的物品最大价值方案。
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] weight = {3, 4, 2, 5, 8, 7, 5};
        int[] value = {4, 6, 3, 8, 13, 12, 11};
        caleMaxValue(weight, value, 20);
    }

    private static void caleMaxValue(int[] weight, int[] value, int capacity) {
        boolean[] isPack = new boolean[weight.length];
        //计算出物品的性价比
        double[] ratio = new double[weight.length];
        for (int i = 0; i < weight.length; i++) {
            ratio[i] = weight[i] * 1.0 / value[i];
        }
        double maxRatio;
        int maxValue = 0;
        int maxIndex = -1;
        while (capacity > 0) {
            maxRatio = -1;
            for (int i = 0; i < weight.length; i++) {
                if (!isPack[i] && ratio[i] > maxRatio) {
                    maxRatio = ratio[i];
                    maxIndex = i;
                }
            }
            isPack[maxIndex] = true;
            while (capacity >= weight[maxIndex]) {
                //优先放入性价比最高的物品
                maxValue += value[maxIndex];
                //更新容量
                capacity -= weight[maxIndex];
            }
        }
        System.out.println("最大放入价值为：" + maxValue + "剩余容量为：" + capacity);
    }

}
