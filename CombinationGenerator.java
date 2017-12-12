/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CombinationGenerator<T> {

    /**
     * The array holding indices to elements considered to be in a combination.
     * Only the first {@code k} indices are considered actually to encode an
     * item in a combination.
     */
    private final int[] indexArray;

    private final int t;
    private final int k;
    private boolean started = false;

    public CombinationGenerator(int totalNumberOfItems, int chosenNumberOfItems) {

        if (chosenNumberOfItems > totalNumberOfItems) {
            System.err.println("Not enough items for combination");
        }

        this.t = totalNumberOfItems;
        this.k = chosenNumberOfItems;

        this.indexArray = new int[this.k];

        for (int i = 0; i < this.k; i++) {
            this.indexArray[i] = i;
        }

    }

    private int increase(int index) {
        this.indexArray[index]++;

        if (this.indexArray[index] >= this.t - (this.k - 1 - index)) {
            if (index != 0) {
                this.indexArray[index] = this.increase(index - 1);
            } else {
                throw new Error("");
            }
        }

        return this.indexArray[index] + 1;
    }

    public boolean generateNextCombination() {

        if (!this.started) {
            this.started = true;
//            System.out.println(Arrays.toString(this.indexArray));
            return true;
        }

        if (this.k == 0) {
            return false;
        }
        
        try {
            increase(this.k - 1);
//            System.out.println(Arrays.toString(this.indexArray));
            return true;
        } catch (Error e) {
            return false;
        }

//        for (int i = this.k - 1; i >= 0; i--) {
//            this.indexArray[i]++;
//
//            if (this.indexArray[i] >= this.t) {
//                if (this.indexArray[i - 1] + 2 < this.t) {
//                    this.indexArray[i] = this.indexArray[i - 1] + 2;
//                } else {
//                    return false;
//                }
//            } else {
//                System.out.println(Arrays.toString(this.indexArray));
//                return true;
//            }
//        }
//
//        return false;
    }

    public void loadCombination(List<T> all, List<T> target) {
        Objects.requireNonNull(all, "The list being sampled is null.");
        Objects.requireNonNull(target,
                "The list to hold the combination is null.");

        target.clear();

        for (int i = 0; i < k; ++i) {
            target.add(all.get(indexArray[i]));
        }
    }

    private void checkTotalNumberOfItems(int totalNumberOfItems) {
        if (totalNumberOfItems < 1) {
            throw new IllegalArgumentException(
                    "Total number of items is illegal: "
                    + totalNumberOfItems + ".");
        }
    }
}
