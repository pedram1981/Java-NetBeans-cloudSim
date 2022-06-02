/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationInterQuartileRange;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegression;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegressionRobust;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMaximumCorrelation;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumUtilization;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyRandomSelection;
import java.lang.Math;

public class JavaApplication3 {

   	private static List<Cloudlet> cloudletList;

	private static List<Vm> vmList;

        private static double hostsNumber = 10;
	private static int vmsNumber = 10;
	private static int cloudletsNumber = 10;
        static int[][] people=new int[100][100];
        static double[] cost_people=new double[100];
         
        static int pop_size=10;
       ////////////////////////////////
        static int imp_count=3;
        static int[][] imp=new int[100][100];
        static int[] imp_c=new int[100];
        static double[] P=new double[100];
        static int[] N1=new int[100];
        static double zeta=0.1;
        static double revolution_rate =20;
        static double beta=0.4;
        static double alfa = 1 - beta;
        static double rate_cross =70;
        static double[] g1=new double[100];
        
	public static void main(String[] args) {
                int generation=1;
                initial_country();
                select_imp_first();
                set_C_P();
                set_N();
                int count_generation_=0;
                  int temp=0;
               double temp_c=0;
               double temp_1=0;
                //////////////////////////////
              int c_g=0;
                while (2 <= imp_count && count_generation_ <= generation)
                    {
                         colonization();
                     /*    temp_1=cost_people[0];
                          for(int i1=1;i1<pop_size;i1++)
                              if(cost_people[i1]<temp_1)
                                 temp_1=cost_people[i1];
                          g1[c_g]=temp_1;
                              c_g++;*/
                              
                         count_generation_++;
                    }
            
                 for(int i=0;i<pop_size;i++)
                   for(int j=0;j<pop_size-1;j++)
                   {
                       if(cost_people[j]>cost_people[j+1])
                       {
                           temp_c=cost_people[j];
                           cost_people[j]=cost_people[j+1];
                           cost_people[j+1]=temp_c;
                           for(int k=0;k<cloudletsNumber;k++)
                           {
                               temp=people[j][k];
                               people[j][k]=people[j+1][k];
                               people[j+1][k]=temp;
                           }
                       }
                   }
                cost_show(0);
                
              //  for(int i=0;i<c_g;i++)
                //    Log.printLine(String.format("%.2f", g1[c_g]));
        }
 //------------------------ imprialist-------------------------------
         private static void initial_country() {
            Random x = new Random();
            double k=0;
            for(int i=0;i<pop_size;i++)
            {
                for(int j=0;j<cloudletsNumber;j++)
                {
                    people[i][j]=x.nextInt(vmsNumber);
                }
                k=cost(i);
                cost_people[i]=k;
            }
            for(int i=pop_size;i<30;i++)
            {
                cost_people[i]=1000000;
            }
          }
          
        private static void select_imp_first(){
               int temp=0;
               double temp_c=0;
                 for(int i=0;i<pop_size;i++)
                   for(int j=0;j<pop_size-1;j++)
                   {
                       if(cost_people[j]>cost_people[j+1])
                       {
                           temp_c=cost_people[j];
                           cost_people[j]=cost_people[j+1];
                           cost_people[j+1]=temp_c;
                           for(int k=0;k<cloudletsNumber;k++)
                           {
                               temp=people[j][k];
                               people[j][k]=people[j+1][k];
                               people[j+1][k]=temp;
                           }
                       }
                   }
                 for (int i = 0; i <imp_count; i++)
                 {
                    for(int j=0;j<cloudletsNumber+1;j++)
                   {
                      imp[i][j]=people[i][j];
                      imp[i][cloudletsNumber]=i;
                   }
                    people[i][cloudletsNumber]=-(i+1);
                 }
              
           }
          
        private static void set_C_P(){
               double sum = 0;
             for(int i =0;i<imp_count;i++)
              {
                P[i] =  Math.exp(-alfa * cost_people[i] / cost_people[imp_count-1]);
                sum = P[i] + sum;
              }
               for(int i = 0;i<imp_count;i++)
                 P[i] = P[i] / sum;
        }
         
        private static void set_N(){
             int h = pop_size - imp_count;
             int[] f1=new int[30];
             for(int i = 0;i<pop_size;i++)
             {
                 if(people[i][cloudletsNumber]<0)
                 f1[i]=1;
                 else
                  f1[i]=0;
             }
             Random x = new Random();
             int k1=0;
             float s1=0;
             for(int i = 0;i<imp_count;i++){
               s1=(float)P[i] * h;
               N1[i] = Math.round(s1);
               imp_c[i]=N1[i];
             }
              int o=0;
              for(int i = 0;i<imp_count;i++){
                 for(int j=0;j<N1[i];j++)
                  {
                    while(true)
                     {
                        o=x.nextInt(pop_size);
                        if(f1[o]==0)
                         {
                          f1[o]=1;
                          people[o][cloudletsNumber]=i;
                          break;
                          }
                    }
                  }
                }
        }
        
        private static void colonization()   {
         double rate_ = 0;
            for(int i = 0;i<imp_count;i++)
            {
              rate_ =Math.floor((imp_c[i] * rate_cross) / 100);
                for(int j = 0;j< rate_;j++)
                {
                  Crossover(i,j);
                 }
            }
            mutation();
            for(int i = 0;i<imp_count;i++)
            {
                if (0 <= imp_c[i])
                    select_imp(i);
            }
            Transfer_clony();
            for(int i = 0;i<imp_count;i++){
                if (0 <= imp_c[i])
                    select_imp(i);
            }
        
        }
        
         private static void Transfer_clony(){
            double temp = 0, temp1 = 10000000, temp2 = 0;
            int index = 0, index1 = 0;
            double[] m = new double[100];
            double[] p = new double[100];
            double[] d = new double[100];
            for (int i = 0; i < imp_count; i++)
            {
                if (0 <= imp_c[i])
                {
                    m[i] = toatal_imp_cust(i);
                    if (temp2 < m[i])
                        temp2 = m[i];
                }
            }

            Random r = new Random();
            temp = 0;
            for (int i = 0; i < imp_count; i++)
            {
                if (0 <= imp_c[i])
                {
                    p[i] = Math.exp(-alfa * (m[i] / temp2));
                    d[i] = p[i] - r.nextDouble();
                    if (temp < Math.abs(d[i]))
                    {
                        temp = d[i];
                        index1 = i;
                    }
                    if (Math.abs(d[i]) < temp1)
                    {
                        temp1 = d[i];
                        index = i;
                    }
                }
            }

            {
                int l = 0;
                
                    int h = r.nextInt(imp_c[index1]);
                    int c=0;
                    if(1<imp_c[index1])
                    {
                    for (int i = 0; i < pop_size; i++) {
                        if(people[i][cloudletsNumber]==index1)
                        {
                            if(c==h)
                            {
                             people[i][cloudletsNumber]=index;
                             break;
                            }
                            else
                                c++;
                        }
                    }
                    imp_c[index]++;
                    imp_c[index1]--;
                    }
                    else
                    {
                         for (int i = 0; i < pop_size; i++) {
                        if(people[i][cloudletsNumber]==-(index1+1))
                        {
                            people[i][cloudletsNumber]=index;
                             break;
                        }
                       }
                         imp_c[index]++;
                         imp_c[index1]=0;
                         imp_count--;
                    }
                    }
                
                

            }

        
         
         static double toatal_imp_cust(int imp_number)  {
            double sum = 0, avr, s;
            double d1=0;
            int[] p = new int[100];
            for (int i = 0; i <= pop_size; i++)
            {
              if(people[i][cloudletsNumber]==imp_number )
                {
                sum = sum + cost_people[i];
                }
                
                
            }
            avr = sum / imp_c[imp_number];
            if (imp_c[imp_number] == 0)
                avr = 0;
            for (int j1 = 0; j1 < pop_size; j1++)
            {
                if(people[j1][cloudletsNumber]==-(imp_number+1))
                d1 = cost_people[j1];
            }
            
            return Math.abs(d1 + zeta * avr);
        }
       private static void mutation(){
             Random x = new Random();
             int p=x.nextInt(pop_size);
             int c=x.nextInt(cloudletsNumber);
             int v=x.nextInt(vmsNumber);
             people[p][c]=v;
             cost_people[p]=cost(p);
        }
         
        private static void Crossover(int imp_number, int clony_number){
         Random r = new Random();
            double[] R_ = new double[100];
            int[] p_new1 = new int[10];
            int[] p_new2 = new int[10];
            int[] p_old = new int[10];
            double[] p1 = new double[10];
            
            for (int i = 0; i < cloudletsNumber; i++)
            {
                p1[i] = r.nextInt(2);
                if (p1[i] == 2)
                    p1[i] = 1;
            }
            for (int i = 0; i < cloudletsNumber; i++)
            {
                if (p1[i] == 0)
                {
                    people[pop_size+1][i] = people[clony_number][i];
                }
                else
                {
                    people[pop_size+1][i] = people[imp_number][i];
                }
                if (p1[i] == 1)
                {
                    people[pop_size+2][i] = people[imp_number][i];
                }
                else
                {
                    people[pop_size+2][i] = people[clony_number][i];
                }
                people[pop_size+3][i] = people[clony_number][i];
            }
            double cust_new1;
            double cust_new2;
            double cust_old;
            cust_new1 = cost(pop_size+1);
            cust_new2 = cost(pop_size+2);
            cust_old = cost_people[clony_number];
            if (cust_new1 < cust_old)
            {
                for (int i = 0; i < cloudletsNumber; i++)
                {
                    people[clony_number][i] = people[pop_size+1][i];
                }
                cost_people[clony_number]=cust_new1;
            }

            if (cust_new2 < cust_new1)
            {
                for (int i = 0; i < cloudletsNumber; i++)
                {
                    people[clony_number][i] = people[pop_size+2][i];
                }
                cost_people[clony_number]=cust_new2;
            }

        }
        
        static void select_imp(int imp_number){
            int index=0;
            double c_1=1000000;
            for (int i = 0; i < imp_count; i++) {
                c_1=1000000;
                for (int j = 0; j < pop_size; j++) {
                     if(people[j][cloudletsNumber]==-(i+1) || people[j][cloudletsNumber]==i )
                     {
                       if(cost_people[j]<c_1)
                        {
                          index=j;
                          c_1=cost_people[j];
                        }
                     }
              }
                people[index][cloudletsNumber]=-(i+1);
                people[imp[i][cloudletsNumber]][cloudletsNumber]=i;
                for(int j=0;j<cloudletsNumber+1;j++)
                   {
                      imp[i][j]=people[index][j];
                      imp[i][cloudletsNumber]=index;
                   }
            }
            
           
        }
 
 
//--------------------------- Cost ---------------------------------------------
        private static double cost(int people_id){
             try {
                 
			int num_user = 1; 
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; 

			create_task();
			CloudSim.init(num_user, calendar, trace_flag);

			PowerDatacenter datacenter = createDatacenter("Datacenter_0");

			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			vmList = createVms(brokerId);

			broker.submitVmList(vmList);

			cloudletList = createCloudletList(brokerId,people_id);

			broker.submitCloudletList(cloudletList);

			double lastClock = CloudSim.startSimulation();

			List<Cloudlet> newList = broker.getCloudletReceivedList();
			

			CloudSim.stopSimulation();

			//printCloudletList(newList);

		    int totalTotalRequested = 0;
		    int totalTotalAllocated = 0;
		   // ArrayList<Double> sla = new ArrayList<Double>();
		    

			//Log.printLine();
			//Log.printLine(String.format("Total simulation time: %.2f sec", lastClock));
			//Log.printLine(String.format("Energy consumption: %.2f kWh", datacenter.getPower() / (3600 * 1000)));
			//Log.printLine(String.format("Number of VM migrations: %d", datacenter.getMigrationCount()));
			//Log.printLine();
                        return datacenter.getPower() / (3600 * 1000);
		} catch (Exception e) {
			//e.printStackTrace();
			//Log.printLine("errors happen");
		}
             return 1000000;
             
        }
//-----------------------------------------------------------------------------
        private static void cost_show(int people_id){
             try {
                 
                    
			int num_user = 1; 
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; 

			create_task();
			CloudSim.init(num_user, calendar, trace_flag);

			PowerDatacenter datacenter = createDatacenter("Datacenter_0");

			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			vmList = createVms(brokerId);

			broker.submitVmList(vmList);

			cloudletList = createCloudletList(brokerId,people_id);

			broker.submitCloudletList(cloudletList);

			double lastClock = CloudSim.startSimulation();

			List<Cloudlet> newList = broker.getCloudletReceivedList();
			

			CloudSim.stopSimulation();

			printCloudletList(newList);

		    int totalTotalRequested = 0;
		    int totalTotalAllocated = 0;
		    ArrayList<Double> sla = new ArrayList<Double>();
		    

			Log.printLine();
			Log.printLine(String.format("Total simulation time: %.2f sec", lastClock));
			Log.printLine(String.format("Energy consumption: %.2f kWh", cost_people[0]));
			//Log.printLine(String.format("Number of VM migrations: %d", datacenter.getMigrationCount()));
			Log.printLine();
                        
		} catch (Exception e) {
			//e.printStackTrace();
			//Log.printLine("errors happen");
		}
           }
	private static List<Cloudlet> createCloudletList(int brokerId,int people_id) {
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		

                 int pesNumber=1;       
		for (int i = 0; i < cloudletsNumber; i++) {
                        Cloudlet cloudlet = new Cloudlet(i, length_task[i], pesNumber, fileSize_task[i], outputSize_task[i], new UtilizationModelStochastic(), new UtilizationModelStochastic(), new UtilizationModelStochastic());
                    	cloudlet.setUserId(brokerId);
			cloudlet.setVmId(people[people_id][i]);
			list.add(cloudlet);
                      
                      }
		

		return list;
	}

	static long[] length_task=new long[10];
	static long[] fileSize_task = new long[10];
	static long[] outputSize_task = new long[10];
        
        private static void create_task() {
		length_task[0] = 150000; 
                length_task[1] = 200000; 
                length_task[2] = 190000; 
                length_task[3] = 100000; 
                length_task[4] = 250000; 
                length_task[5] = 300000; 
                length_task[6] = 290000; 
                length_task[7] = 120000; 
                length_task[8] = 270000; 
                length_task[9] = 180000; 
//------------------------------------------------------------------------------		
		fileSize_task[0] = 300; 
                fileSize_task[1] = 200; 
                fileSize_task[2] = 400; 
                fileSize_task[3] = 250; 
                fileSize_task[4] = 430; 
                fileSize_task[5] = 500; 
                fileSize_task[6] = 150; 
                fileSize_task[7] = 350; 
                fileSize_task[8] = 370; 
                fileSize_task[9] = 450; 
//------------------------------------------------------------------------------                
                outputSize_task[0] = 300; 
                outputSize_task[1] = 200; 
                outputSize_task[2] = 400; 
                outputSize_task[3] = 250; 
                outputSize_task[4] = 430; 
                outputSize_task[5] = 500; 
                outputSize_task[6] = 150; 
                outputSize_task[7] = 350; 
                outputSize_task[8] = 370; 
                outputSize_task[9] = 450; 
	}
        
        private static List<Vm> createVms(int brokerId) {
		List<Vm> vms = new ArrayList<Vm>();

		int[] mips = { 250, 500, 750, 1000 }; 
		int pesNumber = 1;
		int ram = 128; 
		long bw = 2500; 
		long size = 2500;
		String vmm = "Xen";
                int i=0;
		//for (int i = 0; i < vmsNumber; i++) {
                i=0;
			vms.add(
				new Vm(i, brokerId, mips[i % mips.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips[i % mips.length], pesNumber))
			);
		//}
                int[]  mips1 = { 100, 200, 400,300 }; 
		 pesNumber = 1;
		 ram = 128; 
		 bw = 200; 
		size = 2500;
		vmm = "Xen";
                 i=1;
			vms.add(
				new Vm(i, brokerId, mips1[i % mips1.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips1[i % mips1.length], pesNumber))
			);
	
                 int[]  mips2 = { 250, 250, 500,350 }; 
		 pesNumber = 1;
		 ram = 500; 
		 bw = 250; 
		size = 2000;
		vmm = "Xen";
                i=2;
	
			vms.add(
				new Vm(i, brokerId, mips2[i % mips2.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips2[i % mips2.length], pesNumber))
			);
	
                 int[]  mips3 = { 150, 300, 450,600 }; 
		 pesNumber = 1;
		 ram = 400; 
		 bw = 250; 
		size = 3500;
		vmm = "Xen";
                 i=3;
	         	vms.add(
				new Vm(i, brokerId, mips3[i % mips3.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips3[i % mips3.length], pesNumber))
			);
		
                 int[]  mips4 = { 150, 300, 500,400 }; 
		 pesNumber = 1;
		 ram = 500; 
		 bw = 300; 
		size = 4000;
		vmm = "Xen";
                i=4;
			vms.add(
				new Vm(i, brokerId, mips4[i % mips4.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips4[i % mips4.length], pesNumber))
			);
		
                 int[]  mips5 = { 100, 300, 400,500 }; 
		 pesNumber = 1;
		 ram = 256; 
		 bw = 400; 
		size = 3000;
		vmm = "Xen";
                i=5;
			vms.add(
				new Vm(i, brokerId, mips5[i % mips5.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips5[i % mips5.length], pesNumber))
			);

                 int[]  mips6 = { 300, 600, 500,400 }; 
		 pesNumber = 1;
		 ram = 500; 
		 bw = 250; 
		size = 3000;
		vmm = "Xen";
                i=6;
			vms.add(
				new Vm(i, brokerId, mips6[i % mips6.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips6[i % mips6.length], pesNumber))
			);
		
                 int[]  mips7 = { 250, 250, 650,550 }; 
		 pesNumber = 1;
		 ram = 500; 
		 bw = 250; 
		size = 3000;
		vmm = "Xen";
                i=7;
			vms.add(
				new Vm(i, brokerId, mips7[i % mips7.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips7[i % mips7.length], pesNumber))
			);
		
                 int[]  mips8 = { 100, 150, 500,400 }; 
		 pesNumber = 1;
		 ram = 256; 
		 bw = 300; 
		size = 3000;
		vmm = "Xen";
                i=8;
			vms.add(
				new Vm(i, brokerId, mips8[i % mips8.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips8[i % mips8.length], pesNumber))
			);
		
                 int[]  mips9 = { 100, 200, 400,500 }; 
		 pesNumber = 1;
		 ram = 256; 
		 bw = 500; 
		size = 3500;
		vmm = "Xen";
                i=9;
			vms.add(
				new Vm(i, brokerId, mips9[i % mips9.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips9[i % mips9.length], pesNumber))
			);
		return vms;
	}
        

	private static PowerDatacenter createDatacenter(String name) throws Exception {
		List<PowerHost> hostList = new ArrayList<PowerHost>();

		double maxPower = 250; 
		double staticPowerPercent = 0.7; 

		int[] mips = { 1000, 2000, 3000 };
		int ram = 10000; 
		long storage = 1000000;
		int bw = 100000;

		for (int i = 0; i < hostsNumber; i++) {
			
			List<Pe> peList = new ArrayList<Pe>();
			peList.add(new Pe(0, new PeProvisionerSimple(mips[i % mips.length]))); 

			hostList.add(
				new PowerHost(
					i,
					new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw),
					storage,
					peList,
                                         
					new VmSchedulerTimeShared(peList), 
					new PowerModelLinear(maxPower, staticPowerPercent)
				)
			); 
		}

		String arch = "x86"; 
		String os = "Linux"; 
		String vmm = "Xen";
		double time_zone = 10.0;
		double cost = 3.0; 
		double costPerMem = 0.05;
		double costPerStorage = 0.001;
						
		double costPerBw = 0.0; 

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		
		PowerDatacenter powerDatacenter = null;
		try {
                    
			powerDatacenter = new PowerDatacenter(
					name,
					characteristics,
					new PowerVmAllocationPolicySimple(hostList),
					new LinkedList<Storage>(),
					5.0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return powerDatacenter;
	}

	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "\t";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId());

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.printLine(indent + "SUCCESS"
					+indent + cloudlet.getVmId()
					+ indent + dft.format(cloudlet.getActualCPUTime())
					+ indent + dft.format(cloudlet.getExecStartTime())
					+ indent + indent + dft.format(cloudlet.getFinishTime())
				);
			}
		}
	}

}
