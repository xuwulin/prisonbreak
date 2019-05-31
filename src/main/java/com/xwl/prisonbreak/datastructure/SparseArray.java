package com.xwl.prisonbreak.datastructure;

import java.io.*;

/**
 * @Auther: xwl
 * @Date: 2019/5/31 11:37
 * @Description: 稀疏数组
 * 举例，下面就是一个稀疏数组：
 * 11  11  3
 * 1   2   1
 * 2   3   2
 * 4   5   2
 * 其中：
 * 第一行第一列是原始二维数组的行数
 * 第一行第二列是原始二维数组的列数
 * 第一行第三列是原始二维数组非0的个数
 * <p>
 * 第二行以后。。的第一列表示原始二维数组中非0的行数，第二列表示原始二维数组中的非0的列数，第三列则为值
 */
public class SparseArray {

    public static void main(String[] args) {
        // 创建一个原始的二维数组 11 * 11
        // 0: 表示没有棋子， 1 表示 黑子 2 表蓝子
        int chessArr1[][] = new int[11][11];
        chessArr1[1][2] = 1;
        chessArr1[2][3] = 2;
        chessArr1[4][5] = 2;
        // 输出原始的二维数组
        System.out.println("原始的二维数组~~");
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	1	0	0	0	0	0	0	0	0
//      0	0	0	2	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	2	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
//      0	0	0	0	0	0	0	0	0	0	0
        // 使用增强for循环遍历二位数组
        for (int[] row : chessArr1) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }

        // 将二维数组 转 稀疏数组的思路
        // 转成的稀疏数组为
        // 11  11  3
        // 1   2   1
        // 2   3   2
        // 4   5   2
        // 1. 先遍历二维数组 得到非0数据的个数
        int sum = 0;
        // 便利行
        for (int i = 0; i < 11; i++) {
            // 遍历列
            for (int j = 0; j < 11; j++) {
                if (chessArr1[i][j] != 0) {
                    sum++;
                }
            }
        }

        // 2. 创建对应的稀疏数组，非0的个数有sum个，稀疏数组的行就有sum + 1行，列是固定的3列
        int sparseArr[][] = new int[sum + 1][3];
        // 给稀疏数组赋值
        // 第一行第一列是原始二维数组的行数
        sparseArr[0][0] = 11;
        // 第一行第二列是原始二维数组的列数
        sparseArr[0][1] = 11;
        // 第一行第三列是原始二维数组非0的个数
        sparseArr[0][2] = sum;

        // 遍历二维数组，将非0的值存放到 sparseArr中
        // count 用于记录是第几个非0数据
        int count = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (chessArr1[i][j] != 0) {
                    count++;
                    sparseArr[count][0] = i;
                    sparseArr[count][1] = j;
                    sparseArr[count][2] = chessArr1[i][j];
                }
            }
        }

        // 输出稀疏数组
        System.out.println("转换成的稀疏数组~~");
        for (int i = 0; i < sparseArr.length; i++) {
            System.out.printf("%d\t%d\t%d\t\n", sparseArr[i][0], sparseArr[i][1], sparseArr[i][2]);
        }

        // 将稀疏数组写入文件
        String filePath = "D:\\workspace_idea\\prisonbreak\\src\\main\\java\\com\\xwl\\prisonbreak\\datastructure\\file";
        String fileName = "sparseArr";
        saveDoubleArraysToFile(filePath, fileName, sparseArr);

        // 读取文件，将文件内容转为稀疏数组
        FileReader file = null;
        try {
            file = new FileReader("D:\\workspace_idea\\prisonbreak\\src\\main\\java\\com\\xwl\\prisonbreak\\datastructure\\file\\sparseArr.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("文件测试数据如下：");
        txtString(file);

        // 将稀疏数组 --> 恢复成 原始的二维数组
        // 1. 先读取稀疏数组的第一行，根据第一行的数据，创建原始的二维数组
        int chessArr2[][] = new int[sparseArr[0][0]][sparseArr[0][1]];
        // 2. 在读取稀疏数组后几行的数据(从第二行开始)，并赋给 原始的二维数组 即可
        for (int i = 1; i < sparseArr.length; i++) {
            chessArr2[sparseArr[i][0]][sparseArr[i][1]] = sparseArr[i][2];
        }

        // 输出恢复后的二维数组
        System.out.println();
        System.out.println("恢复后的二维数组");
        for (int[] row : chessArr2) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public static File createFile(String filePath, String fileName) {
        File dir = new File(filePath);
        if (dir.exists()) {
            System.out.println("创建目录" + filePath + "失败，目标目录已经存在");
        }
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        // 创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + filePath + "成功！");
        } else {
            System.out.println("创建目录" + filePath + "失败！");
        }
        File file = new File(filePath + fileName + ".txt");
        System.out.println(file + "file");
        //如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(fileName + ".txt文件不存在");
        }
        return file;
    }


    /**
     * 写入二维数组到文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param arrays   二维数组
     */
    public static void saveDoubleArraysToFile(String filePath, String fileName, int[][] arrays) {

        File file = createFile(filePath, fileName);

        BufferedWriter writer = null;

        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            // 遍历行
            for (int i = 0; i < arrays.length; i++) {
                // 遍历列
                for (int j = 0; j < arrays[i].length; j++) {
                    writer.write(arrays[i][j] + "\t");
                }
                writer.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    /**
     * 写入json数据到文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param data     json数据
     */
    public void saveDataToFile(String filePath, String fileName, String data) {
        BufferedWriter writer = null;
        File file = createFile(filePath, fileName);
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    /**
     * 读取文件
     *
     * @param fileName
     * @return
     */
    public String readFileToData(String fileName) {
        // 为了确保文件一定在之前是存在的，将字符串路径封装成File对象
        File file = new File("D:\\workspace_idea\\prisonbreak\\src\\main\\java\\com\\xwl\\prisonbreak\\datastructure\\file\\" + fileName + ".txt");
        if (!file.exists()) {
            //throw new RuntimeException("要读取的文件不存在");
            return null;
        }
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("文件读取成功");
        return laststr;
    }

    /**
     * 读取文件，并将文件内容转为二维数组
     *
     * @param file
     */
    public static void txtString(FileReader file) {
        // 读取文件
        BufferedReader br = new BufferedReader(file);
        try {
            // 读取一行数据
            String line = br.readLine();
            // 将数据转化为int类型
            int lines = Integer.parseInt(line);
            System.out.println(lines);

            String[] sp = null;
            String[][] c = new String[lines][lines];
            int[][] cc = new int[lines][lines];
            int count = 0;
            // 按行读取
            while ((line = br.readLine()) != null) {
                // 按空格进行分割
                sp = line.split(" ");
                for (int i = 0; i < sp.length; i++) {
                    c[count][i] = sp[i];
                }
                count++;
            }
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < lines; j++) {
                    cc[i][j] = Integer.parseInt(c[i][j]);
                    System.out.print(cc[i][j]);
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
