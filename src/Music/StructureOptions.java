package Music;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import Music.MenuAudio;
import static java.lang.System.exit;

public class StructureOptions {
    public String file = "Options.txt";//IN......//OUT
    String tagin = "//IN";
    public List<String> listplayAudio = new ArrayList<>();
    String tagout = "//OUT";
    String outDir = "RecordsAudio";
    String playTemDir = "Temp";

    File Option = new File(file);
    File theDir = new File(outDir);
    File theTempDir = new File(playTemDir);
    DataOutputStream outFile;
    DataInputStream inFile;//
    public FileInputStream inFile1;

    public void read()throws IOException{
        Scanner s1 = new Scanner(Option);
        while(s1.hasNext())
        {
            String ss = s1.nextLine();
            System.out.println(ss);
        }
    }
    public void starterWriterFileStructure()throws IOException{

        if (!Option.exists()) {
            Option.createNewFile();
            outFile = new DataOutputStream(new FileOutputStream(Option));
            outFile.writeBytes(file+"\n");
            Scanner s = new Scanner(System.in);
            System.out.println("Добавить папку с музыкой");
            String str1 = s.nextLine();

            outFile.writeBytes(tagin+"\n");
            outFile.writeBytes(str1+"\n");
            outFile.writeBytes(tagout+"\n");
            outFile.writeBytes(outDir+"\n");
            outFile.writeBytes(playTemDir+"\n");
            outFile.flush();
            outFile.close();
        }

        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        if (!theTempDir.exists()) {
            theTempDir.mkdirs();
        }
    }
    public void writePlayer() throws IOException {

        inFile1 = new FileInputStream(file);
        byte[] b = new byte[inFile1.available()];
        inFile1.read(b,0,inFile1.available());
        String s = new String(b);
        String str2 = "";
        String[] words = s.split("\n");
        Scanner s4 = new Scanner(System.in);

        for(int x=0;x< words.length;++x)
        {
            System.out.println(words[x]);
            str2 +=words[x]+"\n";
            if(Objects.equals(words[x], "//IN"))
            {
                System.out.println("Добавить папку с музыкой");
                String str1 = s4.nextLine();
                str2+=str1+"\n";
            }
        }
        System.out.println(str2);
        inFile1.close();
        FileOutputStream outFile = new FileOutputStream(file);
        outFile.write(str2.getBytes());
        outFile.close();
    }
    public void writeRecPlayer() throws IOException {

        inFile1 = new FileInputStream(file);
        byte[] b = new byte[inFile1.available()];
        inFile1.read(b,0,inFile1.available());
        String s = new String(b);
        String str2 = "";
        String[] words = s.split("\n");
        Scanner s4 = new Scanner(System.in);

        for(int x=0;x< words.length;++x)
        {
            System.out.println(words[x]);
            str2 +=words[x]+"\n";
            if(Objects.equals(words[x], "//OUT"))
            {
                System.out.println("Введите путь к папке");
                String str1 = s4.nextLine();
                str2+=str1+"\n";
            }
        }
        System.out.println(str2);
        inFile1.close();
        FileOutputStream outFile = new FileOutputStream(file);
        outFile.write(str2.getBytes());
        outFile.close();
    }
    public void input(Scanner s) throws Exception {


        int x = s.nextInt();


            switch (x) {
                case 1: {
                    System.out.println("Добавить папку с музыкой");
                    writePlayer();
                }
                break;
                case 2: {
                    System.out.println("Добавить папку где будет храниться запись");
                    writeRecPlayer();
                }
                break;
                case 3: {
                    System.out.println("Посмотреть файл");
                    read();

                }
                break;
                case 4: {
                    //break;
                    MenuAudio.mainLoop();

                }
                break;
            }

    }
    public void menu() throws Exception {

        //input(s);
        //Thread thread = new Thread(task);
        while(true){
            Scanner s = new Scanner(System.in);
            //thread.start();
            System.out.print("\n");
            //thread.start();
            System.out.print("\n");
            System.out.println("1. Добавить папку с музыкой");
            System.out.print("2. Добавить каталог где будут храниться записи.\n");
            System.out.print("3. Посмотреть файл.\n");
            System.out.print("4. Вернуться в предыдущее меню.\n");
            System.out.print("\n");



            input(s);
            if(!s.equals(4))break;
            //thread.stop();

        }
        //s.close();
    }
}
