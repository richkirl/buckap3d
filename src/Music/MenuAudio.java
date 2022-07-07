package Music;

//import com.company.graphic.TestLogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//import static com.company.graphic.TextRender.renderTest;
import static java.lang.System.exit;

enum Menu {
    menu,
    capture,
    options,
    exit
}
//System.out.println("1: Проигрывание музыки.");
//System.out.println("2: Запись музыки.");
//System.out.println("3: Настройки.");
public class MenuAudio {

    private static HashMap<Integer,String> mymenumap = new HashMap<>();

    Menu statep;
    String mainstr="1: Проигрывание музыки.";
    String recstr="2: Запись музыки.";
    String optstr="3: Настройки.";
    String exitstr="4: Выход.";
    //Scanner in = new Scanner(System.in);
    public MenuAudio() {
        this.mymenumap.put(Menu.menu.ordinal(),mainstr);
        this.mymenumap.put(Menu.capture.ordinal(),recstr);
        this.mymenumap.put(Menu.options.ordinal(),optstr);
        this.mymenumap.put(Menu.exit.ordinal(),exitstr);
    }

    public static void renderMenu() throws Exception {
        //File file = new File("tmp.png");
        //System.out.print("1: Проигрывание музыки.");
        //System.out.print("2: Запись музыки.");
        //System.out.print("3: Настройки.");
        //System.out.print("4: Выход.");
    	//"1: Проигрывание музыки."
    }
    public static void input(Scanner s) throws Exception {
        //Scanner s = new Scanner(System.in);
        int x = s.nextInt();
        switch(x){
            case 1:
            {
                StructureOptions structureOptions=new StructureOptions();

                FileInputStream inFile1 = new FileInputStream(structureOptions.file);
                byte[] b = new byte[inFile1.available()];
                inFile1.read(b,0,inFile1.available());
                String s1 = new String(b);

                String[] words = s1.split("\n");


                for(int i=0;i< words.length;++i)
                {
                    if(Objects.equals(words[i], "//IN"))
                    {
                        ++i;
                        while(!Objects.equals(words[i], "//OUT")){
                            if(Objects.equals(words[i], "//OUT"))break;
                            System.out.println(words[i]);
                            structureOptions.listplayAudio.add(words[i]);
                            ++i;
                        }
                    }
                }
                inFile1.close();
                System.out.println("\n");
                //System.out.println((structureOptions.listplayAudio.get(0)));
                //List<String> list = new ArrayList<>();
                for(int y=0;y<structureOptions.listplayAudio.size();++y) {
                    File f = new File(structureOptions.listplayAudio.get(y)); // current directory
                    File[] files = f.listFiles();
                    for (File file : files) {

                        if(file.isFile()) {
                            if(file.getName().contains(".mp3")) {
                                System.out.print("file:\t");
                                System.out.println(file.getName());
                                LameTest.test(file.getAbsolutePath());
                            }
                            if(file.getName().contains(".ogg")) {
                                System.out.print("file:\t");
                                System.out.println(file.getName());
                                //list.add(file.getAbsolutePath());
                                VorbisTest.play(file.getAbsolutePath());
                            }
                            if(file.getName().contains(".wav")) {
                                System.out.print("file:\t");
                                System.out.println(file.getName());
                                //list.add(file.getAbsolutePath());
                                WavFormS.play(file.getAbsolutePath());
                            }
                        }
                    }
                }

                //VorbisTest playAudio = new VorbisTest();
                //playAudio.play();
                //while()
            }
            break;
            case 2:
            {
                CaptureDeviceTest test = new CaptureDeviceTest();
                test.captureTest();
            }
            break;
            case 3:
            {
                StructureOptions structureOptions = new StructureOptions();
                System.out.print("Настройка проигрывателя, записи.\n");
                structureOptions.menu();
                //structureOptions.read();
            }
            break;
            case 4:
            {
                exit(0);
            }
            break;
        }
    }
    public static void mainLoop() throws Exception {
        //int l;
        //renderMenu();
        /*String str="-/---\\-/-|";
        var ref = new Object() {
            int count = 1;
        };
        Runnable task = () ->{
            while(true){

                System.out.print(str.charAt(ref.count) + "\r");
                ref.count = ref.count +2;
                if(ref.count ==9) {
                    System.out.print(str.charAt(ref.count) + "\r");
                    ref.count = 1;
                }
            }
        };*/
        Scanner s = new Scanner(System.in);
        //input(s);
        //Thread thread = new Thread(task);
         while(!s.equals(4)){
             //thread.start();
             System.out.print("\n");
             //thread.start();
             System.out.print("\n");
             renderMenu();
             System.out.print("\n");


             input(s);
             //thread.stop();

        }
    }
}
