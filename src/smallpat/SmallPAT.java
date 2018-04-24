/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smallpat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.JButton;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
/**
 *
 * @author rituranjan
 */
public class SmallPAT extends ApplicationFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SmallPAT.class.getName());
    private int numberOfTraces = 0;
    private int numberOfTracePoints = 0;
    private File traceFile;
    private BufferedReader br;
    private double[][] traceMatrix;
    private String[] plainText;
    private String[] cipherText;
    private int[][] hypothesis;
    private double cor[][];
    private int key[];
    private int plaintextCol;
    private int extraColFront;
    private int extraColRear;
    private String cipher;
    private int tracesToBeUsed;
    private String supportedCiphers[] = {"AES128"};

    public SmallPAT(final String title) { //for testing the UI

        super(title);
        final XYSeries series = new XYSeries("Random Data");
        series.add(1.0, 500.2);
        series.add(5.0, 694.1);
        series.add(4.0, 100.0);
        series.add(12.5, 734.4);
        series.add(17.3, 453.2);
        series.add(21.2, 500.2);
        series.add(21.9, null);
        series.add(25.6, 734.4);
        series.add(30.0, 453.2);
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "XY Series Demo",
            "X", 
            "Y", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JButton jb = new JButton("close");
        //this.setLayout(null);
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call another method in the same class which will close this Jframe
                //setVisible(false);
                dispose();
            }
        });
        this.add(jb, BorderLayout.PAGE_START);
        jb.setLocation(100, 100);
    }

    public SmallPAT(int par1, int par2, int par3, String par4, int par5){ //
        super("TraceTest");
        plaintextCol = par1;
        extraColFront = par2;
        extraColRear = par3;
        cipher = par4;
        tracesToBeUsed = par5;
        LOGGER.info("Creating Trace object");
    }

    public void init(final File fileName) throws IOException{//initializing
        LOGGER.info("Initializing Trace object");
        traceFile = fileName;
        br = new BufferedReader(new FileReader(traceFile));
        int count = 1; //starts from 1 since first line is used to count trace points
        numberOfTracePoints = br.readLine().split(",").length-(extraColFront+extraColRear);
        while(br.readLine()!=null)	count++;
        numberOfTraces = count;
        if(tracesToBeUsed==0){
            tracesToBeUsed = numberOfTraces;
        }
        br.close();
    }
    public int getNumberOfTraces(){
            return numberOfTraces;
    }

    public int getNumberOfTracePoints(){
            return numberOfTracePoints;
    }

    //Function to plot original trace, takes trace number as input
    public void plot(int traceNumber)throws IOException{
        if(traceNumber<1 || traceNumber>numberOfTracePoints){
                System.out.println("Error: Trace not found. Out of bounds.");
        }else{
            LOGGER.info("Starting plot");
            String line = null;
            int count = 0;
            final XYSeries series = new XYSeries("Power Traces");
            br = new BufferedReader(new FileReader(traceFile));
            while(count++<traceNumber-1) br.readLine();

            line = br.readLine();
            System.out.println(line);
            String temp[] = line.split(",");
            for(int x=0;x<numberOfTracePoints;x++){
                    series.add(x,Double.parseDouble(temp[x+2]));
            }

            br.close();
            final XYSeriesCollection data = new XYSeriesCollection(series);
            final JFreeChart chart = ChartFactory.createXYLineChart(
                            "Power Trace Plot","X","Y",data,PlotOrientation.VERTICAL,true,true,false);

            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);

            setResizable(false);
            setUndecorated(true);
            LOGGER.info("Plotting success!");
        }
    }

    public void initTraceMatrix()throws IOException{
        br = new BufferedReader(new FileReader(traceFile));
        traceMatrix = new double[tracesToBeUsed][numberOfTracePoints];
        plainText = new String[tracesToBeUsed];
        cipherText= new String[tracesToBeUsed];
        String t[];
        for(int count=0;count<tracesToBeUsed;count++){
            t = br.readLine().split(",");
            plainText[count] = t[plaintextCol];
            cipherText[count]= t[1];
            for(int j=extraColFront;j<numberOfTracePoints-extraColRear;j++){
                traceMatrix[count][j-extraColFront] = Double.parseDouble(t[j]);
            }
        }
        br.close();
    }

    public int HW(int n){
            return Integer.bitCount(n);
    }

    public int hex2int(String hex){
            HashMap<String, Integer> hexmap = new HashMap<String, Integer>();
            hexmap.put("0", 0);hexmap.put("1", 1);hexmap.put("2", 2);hexmap.put("3", 3);
            hexmap.put("4", 4);hexmap.put("5", 5);hexmap.put("6", 6);hexmap.put("7", 7);
            hexmap.put("8", 8);hexmap.put("9", 9);hexmap.put("A", 10);hexmap.put("B", 11);
            hexmap.put("C", 12);hexmap.put("D", 13);hexmap.put("E", 14);hexmap.put("F", 15);

            return (hexmap.get(hex.charAt(0)+"") << 4) ^ (hexmap.get(hex.charAt(1)+""));
    }

    public int SBOX(int a){
        //256
        int s[] = { 99 , 124 , 119 , 123 , 242 , 107 , 111 , 197 , 48 , 1 , 103 , 43 , 254 , 215 , 171 , 118 , 
                    202 , 130 , 201 , 125 , 250 , 89 , 71 , 240 , 173 , 212 , 162 , 175 , 156 , 164 , 114 , 192 , 
                    183 , 253 , 147 , 38 , 54 , 63 , 247 , 204 , 52 , 165 , 229 , 241 , 113 , 216 , 49 , 21 , 
                    4 , 199 , 35 , 195 , 24 , 150 , 5 , 154 , 7 , 18 , 128 , 226 , 235 , 39 , 178 , 117 , 
                    9 , 131 , 44 , 26 , 27 , 110 , 90 , 160 , 82 , 59 , 214 , 179 , 41 , 227 , 47 , 132 , 
                    83 , 209 , 0 , 237 , 32 , 252 , 177 , 91 , 106 , 203 , 190 , 57 , 74 , 76 , 88 , 207 , 
                    208 , 239 , 170 , 251 , 67 , 77 , 51 , 133 , 69 , 249 , 2 , 127 , 80 , 60 , 159 , 168 , 
                    81 , 163 , 64 , 143 , 146 , 157 , 56 , 245 , 188 , 182 , 218 , 33 , 16 , 255 , 243 , 210 , 
                    205 , 12 , 19 , 236 , 95 , 151 , 68 , 23 , 196 , 167 , 126 , 61 , 100 , 93 , 25 , 115 , 
                    96 , 129 , 79 , 220 , 34 , 42 , 144 , 136 , 70 , 238 , 184 , 20 , 222 , 94 , 11 , 219 , 
                    224 , 50 , 58 , 10 , 73 , 6 , 36 , 92 , 194 , 211 , 172 , 98 , 145 , 149 , 228 , 121 , 
                    231 , 200 , 55 , 109 , 141 , 213 , 78 , 169 , 108 , 86 , 244 , 234 , 101 , 122 , 174 , 8 , 
                    186 , 120 , 37 , 46 , 28 , 166 , 180 , 198 , 232 , 221 , 116 , 31 , 75 , 189 , 139 , 138 , 
                    112 , 62 , 181 , 102 , 72 , 3 , 246 , 14 , 97 , 53 , 87 , 185 , 134 , 193 , 29 , 158 , 
                    225 , 248 , 152 , 17 , 105 , 217 , 142 , 148 , 155 , 30 , 135 , 233 , 206 , 85 , 40 , 223 , 
                    140 , 161 , 137 , 13 , 191 , 230 , 66 , 104 , 65 , 153 , 45 , 15 , 176 , 84 , 187, 22 };
        return s[a];
    }

    public void initHypothesis_MCU8_AES128(int byteNumber)throws IOException{
        //Considering function P XOR K => HW(P^Ki) where i=0 to 255
        //Hypothesis matrix is (numberOfTraces X 256)  
        //byteNumber -> 1,2,3,..,16
        //		(2(n-1))  2n
        //byte 1 = (0) , (2*1)
        //byte 2 = (2) , (2*2)
        //byte 3 = (4) , (2*3)

        int keyHyp[] = new int[256];
        for(int i=0;i<255;i++){
                keyHyp[i] = i;
        }
        hypothesis = new int[plainText.length][keyHyp.length];
        for(int i=0;i<plainText.length;i++){
            String P = plainText[i].substring((2*(byteNumber-1)), (2*byteNumber)); //nth byte of plaintext block
            for(int j=0;j<keyHyp.length;j++){
                    //HW(P XOR keyHyp[j])
                    hypothesis[i][j] = HW(SBOX(hex2int(P) ^ keyHyp[j]));
            }
        }
    }

    public double Correlation(double[] xs, double[] ys) {
        //TODO: check here that arrays are not null, of the same length etc

        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        int n = xs.length;

        for(int i = 0; i < n; ++i) {
          double x = xs[i];
          double y = ys[i];

          sx += x;
          sy += y;
          sxx += x * x;
          syy += y * y;
          sxy += x * y;
        }

        // covariation
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);

        // correlation is just a normalized covariation
        return cov / sigmax / sigmay;
      }

    public void findCorrelation(){
        //correlation matrix 256 X numberOfTracePoints
        cor = new double[256][numberOfTracePoints];
        double x[] = new double[tracesToBeUsed];
        double y[] = new double[tracesToBeUsed];
        int count = 0;
        while(count<=255){
            for(int j=0;j<tracesToBeUsed;j++){
                y[j] = (double)hypothesis[j][count]/256;
            }
            for(int i=0;i<numberOfTracePoints;i++){
                for(int j=0;j<tracesToBeUsed;j++){
                    x[j] = traceMatrix[j][i];
                }
//		    cor[count][i] = new PearsonsCorrelation().correlation(x, y);
                cor[count][i] = Correlation(x,y);
                }
            count++;
        }
    }

    public int findKey(){
        double max = cor[0][0];int loc=0;
        for(int i=0;i<256;i++){
            for(int j=0;j<numberOfTracePoints;j++){
                if(cor[i][j]>max){
                    max = cor[i][j];
                    loc = i;
                }
            }
        }
        return loc;
    }

//	public void saveCorrMatrix() throws IOException{
//		File f = new File("correlationmatrix.csv");
//		f.createNewFile();
//		FileWriter fw = new FileWriter(f);
//		BufferedWriter bw = new BufferedWriter(fw);
//		PrintWriter pw = new PrintWriter(bw);
//		for(int i=0;i<256;i++){
//			for(int j=0;j<numberOfTracePoints;j++){
//				pw.print(cor[i][j]+",");
//			}pw.println();
//		}
//		pw.close(); bw.close(); fw.close();
//	}

    public HashMap<Object,Object> CPA(int keysize)throws IOException{
            key = new int[keysize];
            //Form Trace Matrix
            LOGGER.info("Running Analysis...");
            initTraceMatrix();
            long start = System.currentTimeMillis();
            for(int i=1;i<=keysize;i++){
                    initHypothesis_MCU8_AES128(i);
                    findCorrelation();
                    key[i-1] = findKey();
            }
            long end = System.currentTimeMillis();
            LOGGER.info("Analysis Complete.");
            double timetaken = ((end-start)/1000);
            String strkey = "", t="";
            for(int i=0;i<keysize;i++){
                t = Integer.toHexString(key[i]).toUpperCase()+" ";
                strkey += (t.length()<3)?("0"+t):t;
                System.out.println("t = "+t+" \t strkey = "+strkey);
            }System.out.println(strkey.trim());
            System.out.println("Total Time: "+timetaken+" seconds");

            HashMap<Object,Object> retVal = new HashMap<Object,Object>();
            retVal.put("key", strkey);
            retVal.put("time", timetaken);
            return retVal;
    }

    public static void main(final String[] args) throws IOException {
        /*final TraceTest demo = new TraceTest("XY Series Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);*/
            File f = new File("Path to file");
            SmallPAT ob = new SmallPAT(0,2,0,"AES128",0);
            ob.init(f);
            //plot a trace and display plot(trace_number)
//		ob.plot(3);
//		ob.pack();RefineryUtilities.centerFrameOnScreen(ob);ob.setVisible(true);
            //*ob.CPA(16);
            //BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
            //System.out.println("enter 1 to continue or any other key to exit");
//                while(br.readLine().equals("1")){
//                    System.out.print("Enter hex byte:: ");
//                    System.out.println(ob.hex2int(br.readLine().toUpperCase()));
//                    System.out.println("Press enter to exit or any other key to continue");
//                }
    }
}
