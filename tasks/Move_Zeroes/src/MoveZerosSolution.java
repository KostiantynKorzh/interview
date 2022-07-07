import java.util.Arrays;

class MoveZerosSolution {

    public static void main(String[] args) {
        MoveZerosSolution moveZerosSolution = new MoveZerosSolution();

        int[] a = {0, 1, 0, 3, 12};
        int[] b = {0, 1, 0, 3, 0, 3, 4, 54, 0, 0, 0, 0, 0, 0, 3, 4, 5, 6, 7, 3, 23, 54, 2, 0, 0, 0, 0, 0, 3, 3, 3, 4, 436, 345, 45, 2, 7, 0, 0, 6, 4, 7, 8, 9, 34, 65, 678, 34, 0, 5, 0, 12};
        int[] c = {0};

        moveZerosSolution.moveZeroes(a);
        moveZerosSolution.moveZeroes(b);
        moveZerosSolution.moveZeroes(c);
    }

    public void moveZeroes(int[] nums) {
        int zeros = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                zeros++;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                if (i > nums.length - zeros) {
                    break;
                }
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[j] != 0) {
                        int temp = nums[j];
                        nums[j] = nums[i];
                        nums[i] = temp;
                        break;
                    }
                }
            }
        }

        System.out.println(Arrays.toString(nums));

    }
}