package com.scj.sort;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shengcj on 2016/11/15.
 */
public class BucketSort
{
    /**
     * 测试..... 这里的测试数据是一个含n个元素的数组，且每个元素满足0<=arr[i]<1
     */
    public static void main(String[] args) {
        double arr[] = { 0.78, 0.17, 0.39, 0.26, 0.72, 0.94, 0.21, 0.12, 0.23, 0.68 ,0.13, 0.13,0.67,0.86,0.69};
        bucketSort(arr);
        System.out.print("最终排序结果：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+"t");
        }
    }

    /**
     * 桶排序算法，对arr进行桶排序，排序结果仍放在arr中
     *
     * @param arr
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void bucketSort(double arr[]) {
        int n = arr.length;
        ArrayList arrList[] = new ArrayList[n];
        // 把arr中的数均匀的的分布到[0,1)上，每个桶是一个list，存放落在此桶上的元素
        for (int i = 0; i < n; i++) {
            int temp = (int) Math.floor(n * arr[i]); //返回不大于的最大整数
            if (null == arrList[temp]){
                arrList[temp] = new ArrayList();
            }
            arrList[temp].add(arr[i]);
        }
        // 对每个桶中的数进行插入排序
        for (int i = 0; i < n; i++) {
            if (null != arrList[i]){
                insert(arrList[i]);
            }
        }
        // 把各个桶的排序结果合并
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (null != arrList[i]) {
                Iterator iter = arrList[i].iterator();
                while (iter.hasNext()) {
                    Double d = (Double) iter.next();
                    arr[count] = d;
                    count++;
                }
            }
        }
    }

    /**
     * 用插入排序对每个桶进行排序
     *
     * @param list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void insert(ArrayList list) {
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                if ((Double) list.get(i) < (Double) list.get(i - 1)) {
                    double temp = (Double) list.get(i);
                    int j = i - 1;
                    for (; j >= 0 && ((Double) list.get(j) >temp); j--){
                        list.set(j + 1, list.get(j));
                    }
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
