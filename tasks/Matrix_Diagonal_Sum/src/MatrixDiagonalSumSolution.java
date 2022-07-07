class MatrixDiagonalSumSolution {

    public static void main(String[] args) {
        MatrixDiagonalSumSolution matrixDiagonalSumSolution = new MatrixDiagonalSumSolution();
        int[][] mat = new int[][]{
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}};

        int[][] mat1 = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};

        System.out.println(matrixDiagonalSumSolution.diagonalSum(mat));
//        System.out.println(solution.diagonalSum(mat1));
    }

    public int diagonalSum(int[][] mat) {
        int length = mat.length;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j || length - i == j + 1) {
                    sum += mat[i][j];
                }
            }
        }


        return sum;
    }
}