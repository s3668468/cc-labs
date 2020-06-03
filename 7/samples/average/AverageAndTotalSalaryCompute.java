package average;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author http://www.devinline.com
 */
public class AverageAndTotalSalaryCompute {

	public static class MapperClass extends 
			Mapper<LongWritable, /*Input key Type */ 
			Text, /*Input value Type*/ 
			Text, /*Output key Type*/ 
			FloatWritable> /*Output value Type*/ 
	{
		//mapper method
		public void map(LongWritable key, Text empRecord, Context con)
				throws IOException, InterruptedException {
			
			//read input line by line, tokenize and create an array of tokens 
			String[] word = empRecord.toString().split("\\t");
			
			//M or F
			String sex = word[3];
			try {
				//get salary
				Float salary = Float.parseFloat(word[8]);
				//create the mapping
				con.write(new Text(sex), new FloatWritable(salary));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ReducerClass extends
			Reducer<Text, /*Input key Type */
			FloatWritable, /*Input value Type*/ 
			Text, /*Output key Type*/
			Text> /*Output value Type*/
	{
		//reduce method
		public void reduce(Text key, Iterable<FloatWritable> valueList,
				Context con) throws IOException, InterruptedException {
			try {
				Float total = (float) 0;
				int count = 0;
				//for each values inside the list of values corresponding
				//to this key
				for (FloatWritable var : valueList) {
					total += var.get(); //calculate the total
					System.out.println("reducer " + var.get());
					count++;
				}
				Float avg = (Float) total / count; //calculate the average
				String out = "Total: " + total + " :: " + "Average: " + avg;
				con.write(key, new Text(out)); //create the reduced map
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		try {
			//create the job instance and set all properties
			Job job = Job.getInstance(conf, "FindAverageAndTotalSalary");
			job.setJarByClass(AverageAndTotalSalaryCompute.class);
			job.setMapperClass(MapperClass.class);
			job.setReducerClass(ReducerClass.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(FloatWritable.class);
			 Path p1 = new Path(args[0]); //set the input path
			 Path p2 = new Path(args[1]); //set the output path
			 FileInputFormat.addInputPath(job, p1);
			 FileOutputFormat.setOutputPath(job, p2);
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}