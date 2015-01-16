package com.example.genesisforandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

import android.R.array;



public class GetNH {

	Document GradePageDoc;
	String StdID;
	Connection.Response res;
	String pass;
	String user;
	String userMod;
	Integer count=0;
	Boolean sucess=false;
	Map<String, String> cookies;
	String errorType;
	
	public GetNH(String Username, String Password)
	{

        pass=Password;
        user=Username;
        
		  while (count<3) 
		  {
			try 
			{
				res = Jsoup
						.connect("http://parent.northernhighlands.org/nohighlands/parents?gohome=true")
						.method(Method.GET)
						.timeout(500)
						.execute();
				
				cookies = res.cookies();

				res = Jsoup
						.connect("http://parent.northernhighlands.org/nohighlands/j_security_check")
						.data("j_username", user, "j_password", pass)
						.method(Method.POST)
						.userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5")
						.timeout(500)
						.ignoreHttpErrors(true)
						.cookies(cookies)
						.execute();

				cookies=res.cookies();
				
				if (res.parse().title().contentEquals("Parents at Northern Highlands Regional High School")==true)
				{
					String tempString=res.url().toString();
					String tempStringTwo= tempString.replace("http://parent.northernhighlands.org/nohighlands/parents?module=home&studentid=","" );
					StdID=tempStringTwo.replace("&action=form", "");
					
					tempString=null;
					tempStringTwo=null;
					
					if (StdID.length()!=4)
						errorType="Student ID Incorrect";
							
				}
					
				else
				{
					errorType="Bad Username or Password";
					break;
				}
					
					
				

				GradePageDoc=Jsoup
						.connect("http://parent.northernhighlands.org/nohighlands/parents?module=gradebook&studentid="+StdID+"&action=form HTTP/1.1")
						.cookies(cookies)
						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5")
						.referrer("http://parent.northernhighlands.org/nohighlands/parents?module=home&studentid=4979&action=form")
						.get();
				
				count=0;

				break;

			} 
			catch (IOException e) 
			{

				count++;

			}
			if (count==3)
				errorType="Login timed out";
		}
	}

	
	public Document getGradeDoc() 
	{
		return GradePageDoc;
	}
	
	public ArrayList<String> getClasses()
	{
		ArrayList<String> list=new ArrayList<String>();
		
		int y=0;
		while(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listrowodd td.cellLeft span.categorytab font u").eq(y).text().length()>1)
		{
		list.add(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listrowodd td.cellLeft span.categorytab font u").eq(y).text());
		y++;
		}
		
		y=0;
		while(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listrowodd td.cellLeft span.categorytab font u").eq(y).text().length()>1)
		{
		list.add(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listroweven td.cellLeft span.categorytab font u").eq(y).text());
		y++;
		}
			

		return list;
	}
	
	public ArrayList<String> getGrades()
	{
		ArrayList<String> list=new ArrayList<String>();
		for (int x=0;x<5;x++)
			list.add(GradePageDoc.getElementsByClass("listrowodd").get(x).select("td").get(2).text());
			
		for (int x=0; x<4;x++)
			list.add(GradePageDoc.getElementsByClass("listroweven").get(x).select("td").get(2).text());
		return list;
	}
	/*
	
	public array[][] getColorsAndGrades()
	{
		ArrayList<String> temp=this.getGrades();
		String[][] ColorGrade=new String[temp.size()][2];

		
		for(int row=0;row<=temp.size();row++)
		{
			for (int column=0;column<2;column++)
			{
				
			}
		}	
	}
	*/
	
	
	public ArrayList<String> getTeachers()

	{
		//Gets teachers  by selecting every 2nd thingy in the arraylist
		ArrayList<String> list=new ArrayList<String>();
		
		int y=1;
		
		while(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listrowodd td.cellLeft").eq(y).text().length()>1)
		{
			String temp=GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listrowodd td.cellLeft").eq(y).text();
			String teacherName=temp.substring(0, temp.indexOf(" Email"));  //Removes the Email from the teachername
			list.add(teacherName);
			y=y+2;
		}
		
		y=1;
		
		while(GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listroweven td.cellLeft").eq(y).text().length()>1)
		{
			String temp=GradePageDoc.select("html body.parentsBody table tbody tr td table.list tbody tr.listroweven td.cellLeft").eq(y).text();
			String teacherName=temp.substring(0, temp.indexOf(" Email")); //Removes the Email from the teachername
			list.add(teacherName);
			y=y+2;
		}
		
		return list;
	}
	public String getErrorType()
	{
		return errorType;
	}
	

}
