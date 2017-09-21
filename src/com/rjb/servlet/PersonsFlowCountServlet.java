package com.rjb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.rjb.util.AboutDate;
import com.rjb.util.DBBean;
import com.rjb.util.Return;
/**
 * 客流量统计
 * 
 * @author liying
 *
 */
public class PersonsFlowCountServlet extends HttpServlet
{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		req.setCharacterEncoding("utf-8");
		String method = req.getParameter("method");
		if ("countByDay".equals(method)) {
			try
			{
				countByDay(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//按天统计客流量
		} 
		else if ("countByMonth".equals(method)) {
			try
			{
				countByMonth(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("countRuDianByDay".equals(method)) {
			try
			{
				countRuDianByDay(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("countRuDianByMonth".equals(method)) {
			try
			{
				countRuDianByMonth(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("todayRuDianLv".equals(method)) {
			try
			{
				todayRuDianLv(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("countRuDianLvByMonth".equals(method)) {
			try
			{
				countRuDianLvByMonth(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("countRuDianLvByDay".equals(method)) {
			try
			{
				countRuDianLvByDay(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("xinLaoGuKeZhanBiTJ".equals(method)) {
			try
			{
				xinLaoGuKeZhanBiTJ(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if ("JinRiLaiFangXinLaoZhanBi".equals(method)) {
			try
			{
				JinRiLaiFangXinLaoZhanBi(req, resp);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 按日统计客流量
	 * @throws ParseException 
	 * 
	 */
	private void countByDay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		//System.out.println(startTime+","+endTime);
		
		//将客户端传来的起止日期进行格式转化并计算时间差
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");  
	    Date d1=sdf.parse(startTime);  
	    Date d2=sdf.parse(endTime);
		int days= (int)((d2.getTime()-d1.getTime())/86400000 );
		System.out.println(days);
		
		//将起止时间转化为与数据库格式相同
		startTime = startTime.replace("/", "");
		endTime = endTime.replace("/", "");
		System.out.println(startTime+","+endTime);
		DBBean dbBean = new DBBean();
		int []personNum = new int[days+1];
		for(int i=0;i<=days;i++)
		{			
			String sql="select count(*) from wifimessage where time > "+startTime+"000000 and time < "+startTime+"999999";
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					personNum[i] = rs.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println(personNum[i]);
			AboutDate aboutDate = new AboutDate();
			startTime = aboutDate.getAfterDate(startTime);
			//System.out.println(startTime);
			//System.out.println(sql);
		}
		
		Return result = new Return(days,personNum,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
		
	}
	
	//按月统计客流量
	private void countByMonth(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		//System.out.println("22");
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		startTime=startTime.replace("/", "");
		endTime=endTime.replace("/", "");
		System.out.println(startTime+","+endTime);
		
		//根据客户端传来的起止月份计算时间差
		AboutDate aboutDate = new AboutDate(); 
		int months = aboutDate.getMonthDiff(startTime, endTime);
		System.out.println(months);
		DBBean dbBean = new DBBean();
		int []monthPersonNum = new int[months+1];
		String []allDate = new String[months+1];
		for(int i=0;i<=months;i++)
		{		
			allDate[i]=startTime;
			String sql="select count(*) from wifimessage where time > "+startTime+"00000000 and time < "+startTime+"99999999";
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					monthPersonNum[i] = rs.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println(monthPersonNum[i]);
			startTime = aboutDate.getNextMonth(startTime);
			System.out.println(startTime);
			System.out.println(sql);
		}
		
		Return result = new Return(months,monthPersonNum,allDate);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}
	
	//按日统计入店量
	private void countRuDianByDay(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String rang = req.getParameter("rang");
		//System.out.println(startTime+","+endTime);
		
		//将客户端传来的起止日期进行格式转化并计算时间差
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");  
	    Date d1=sdf.parse(startTime);  
	    Date d2=sdf.parse(endTime);
		int days= (int)((d2.getTime()-d1.getTime())/86400000 );
		System.out.println(days);
		
		//将起止时间转化为与数据库格式相同
		startTime = startTime.replace("/", "");
		endTime = endTime.replace("/", "");
		System.out.println(startTime+","+endTime);
		DBBean dbBean = new DBBean();
		int []personNum = new int[days+1];
		for(int i=0;i<=days;i++)
		{			
			String sql="select count(*) from wifimessage where time > "+startTime+"000000 and time < "+startTime+"999999 and rang < "+rang+"";
			System.out.println(sql);
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					personNum[i] = rs.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println(personNum[i]);
			AboutDate aboutDate = new AboutDate();
			startTime = aboutDate.getAfterDate(startTime);
			//System.out.println(startTime);
			//System.out.println(sql);
		}
		
		Return result = new Return(days,personNum,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}
	
	//按月统计入店量
	private void countRuDianByMonth(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		//System.out.println("22");
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String rang = req.getParameter("rang");
		startTime=startTime.replace("/", "");
		endTime=endTime.replace("/", "");
		System.out.println(startTime+","+endTime);
		
		//根据客户端传来的起止月份计算时间差
		AboutDate aboutDate = new AboutDate(); 
		int months = aboutDate.getMonthDiff(startTime, endTime);
		System.out.println(months);
		DBBean dbBean = new DBBean();
		int []monthPersonNum = new int[months+1];
		String []allDate = new String[months+1];
		for(int i=0;i<=months;i++)
		{		
			allDate[i]=startTime;
			String sql="select count(*) from wifimessage where time > "+startTime+"00000000 and time < "+startTime+"99999999 and rang < "+rang+"";
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					monthPersonNum[i] = rs.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println(monthPersonNum[i]);
			startTime = aboutDate.getNextMonth(startTime);
			System.out.println(startTime);
			System.out.println(sql);
		}
		
		Return result = new Return(months,monthPersonNum,allDate);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}	

	//统计当日入店率
	private void todayRuDianLv(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		req.setCharacterEncoding("utf-8");
		String rang = req.getParameter("rang");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String today = df.format(new Date());
		int ruDian=0;
		int []all=new int[1];
		DBBean dbBean = new DBBean();
		String sql="select count(*) from wifimessage where time > "+today+"000000 and time < "+today+"999999 and rang < "+rang+"";
		System.out.println(sql);
		ResultSet rs = dbBean.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				ruDian = rs.getInt(1);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String sql2="select count(*) from wifimessage where time > "+today+"000000 and time < "+today+"999999 ";
		System.out.println(sql2);
		ResultSet rs2 = dbBean.executeQuery(sql2);
		try
		{
			while(rs2.next())
			{
				all[0] = rs2.getInt(1);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		Return result = new Return(ruDian,all,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}

	//按天统计入店率
	private void countRuDianLvByDay(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		System.out.println("按天统计入店率");
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String rang = req.getParameter("rang");
		System.out.println(startTime+","+endTime);
		
		//将客户端传来的起止日期进行格式转化并计算时间差
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");  
		Date d1=sdf.parse(startTime);  
	    Date d2=sdf.parse(endTime);
	    int days= (int)((d2.getTime()-d1.getTime())/86400000 );
	    System.out.println(days);
		
		startTime=startTime.replace("/", "");
		endTime=endTime.replace("/", "");
		//根据客户端传来的起止月份计算时间差
		AboutDate aboutDate = new AboutDate(); 
		int a = 0,b=1;
		DBBean dbBean = new DBBean();
		int []monthPersonNum = new int[days+1];
		for(int i=0;i<=days;i++)
		{		
			String sql="select count(*) from wifimessage where time > "+startTime+"000000 and time < "+startTime+"999999";
			System.out.println(sql);
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					a = rs.getInt(1);
					if(a==0)
					{
						a=1;
					}
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			String sql2="select count(*) from wifimessage where time > "+startTime+"000000 and time < "+startTime+"999999 and rang < "+rang+"";
			ResultSet rs2 = dbBean.executeQuery(sql2);
			try
			{
				while(rs2.next())
				{
					b = rs2.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(a+";"+b);
			monthPersonNum[i]=b*100/a;
			//System.out.println(monthPersonNum[i]);
			startTime = aboutDate.getAfterDate(startTime);
			//System.out.println(startTime);
			System.out.println(a+";"+b);
		}
		
		Return result = new Return(days,monthPersonNum,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}	

	//按月统计入店率
	private void countRuDianLvByMonth(HttpServletRequest req, HttpServletResponse resp) throws ParseException, JsonGenerationException, JsonMappingException, IOException
	{
		System.out.println("按月统计入店率");
		req.setCharacterEncoding("utf-8");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String rang = req.getParameter("rang");
		startTime=startTime.replace("/", "");
		endTime=endTime.replace("/", "");
		System.out.println(startTime+","+endTime);
		
		//根据客户端传来的起止月份计算时间差
		AboutDate aboutDate = new AboutDate(); 
		int months = aboutDate.getMonthDiff(startTime, endTime);
		//System.out.println(months);
		int a = 0,b=1;
		DBBean dbBean = new DBBean();
		int []monthPersonNum = new int[months+1];
		String []allDate = new String[months+1];
		for(int i=0;i<=months;i++)
		{		
			allDate[i]=startTime;
			String sql="select count(*) from wifimessage where time > "+startTime+"00000000 and time < "+startTime+"99999999";
			System.out.println(sql);
			ResultSet rs = dbBean.executeQuery(sql);
			try
			{
				while(rs.next())
				{
					a = rs.getInt(1);
					if(a==0)
					{
						a=1;
					}
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			String sql2="select count(*) from wifimessage where time > "+startTime+"00000000 and time < "+startTime+"99999999 and rang < "+rang+"";
			ResultSet rs2 = dbBean.executeQuery(sql2);
			try
			{
				while(rs2.next())
				{
					b = rs2.getInt(1);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(a+";"+b);
			monthPersonNum[i]=b*100/a;
			//System.out.println(monthPersonNum[i]);
			startTime = aboutDate.getNextMonth(startTime);
			//System.out.println(startTime);
			System.out.println(a+";"+b);
		}
		
		Return result = new Return(months,monthPersonNum,allDate);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
	}
	
	//新老客户占比统计
	private void xinLaoGuKeZhanBiTJ(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
		
		DBBean dbBean = new DBBean();
		int []personNum = new int[2];			
		String sql="select count(*) from (select count(*) AS counts from wifimessage group by phoneMac) AS a WHERE a.counts>250";
		ResultSet rs = dbBean.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				System.out.println( rs.getInt(1));
				personNum[0] = rs.getInt(1);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql2="select count(*) from (select count(*) AS counts from wifimessage group by phoneMac) AS a";
		ResultSet rs2 = dbBean.executeQuery(sql2);
		try
		{
			while(rs2.next())
			{
				System.out.println( rs2.getInt(1));
				personNum[1] = rs2.getInt(1)-personNum[0];
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		
		Return result = new Return(1,personNum,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
		
	}
	
	//近七日新老客户占比统计
	private void JinRiLaiFangXinLaoZhanBi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String today = df.format(new Date());
		DBBean dbBean = new DBBean();
		int []personNum = new int[2];			
		String sql="select count(*) from (select count(*) AS counts from wifimessage group by phoneMac) AS a WHERE a.counts>250";
		ResultSet rs = dbBean.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				System.out.println( rs.getInt(1));
				personNum[0] = rs.getInt(1);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql2="select count(*) from (select count(*) AS counts from wifimessage where time < "+today+"000000  group by phoneMac ) AS a";
		System.out.println(sql2);
		ResultSet rs2 = dbBean.executeQuery(sql2);
		try
		{
			while(rs2.next())
			{
				System.out.println( rs2.getInt(1));
				personNum[1] = rs2.getInt(1);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		
		Return result = new Return(1,personNum,null);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(result));
		
		resp.setContentType("text/javascript");
		resp.getWriter().print(mapper.writeValueAsString(result));
		
	}
}
