package org.idanso.stationinfo.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.servlet.VelocityServlet;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.ListTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.idanso.dataset.StationRecordsXYDataset;
import org.idanso.stationinfo.StationInfoLauncher;
import org.idanso.stationinfo.util.HibernateUtil;
import org.idanso.stationinfo.util.StationUtils;
import org.idanso.stationinfo.util.TranslationUtils;
import org.idanso.weather.Station;
import org.idanso.weather.StationRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class WeatherServlet extends VelocityServlet {

	private static final String DEFAULT_LANGUAGE = "heb"; //$NON-NLS-1$
	private static final String HTML_MIME = "text/html;charset=utf-8"; //$NON-NLS-1$
	private static final int MAX_RECORDS_IN_PAGE = 25;
	private static final String PATH_SEPERATOR = "/"; //$NON-NLS-1$
	private static final long serialVersionUID = 3690474714699084599L;
	private static final String TEMPLATE_EXT = ".vm"; //$NON-NLS-1$
	
	private static final String XHTML_MIME = "application/xhtml+xml"; //$NON-NLS-1$
	
	protected CacheManager cacheManager;
			
	private Log log;
	private ListTool listTool=new ListTool();
	private DateTool dateTool=new DateTool();
	private MathTool mathTool=new MathTool();
	private NumberTool numberTool=new NumberTool();


	public WeatherServlet()
	{
		super();
	}
	
	public void destroy() {
		super.destroy();
		cacheManager.shutdown();
	}
	
	
	private Template doMonthlyChart(HttpServletRequest req, HttpServletResponse res) throws HibernateException, IllegalStateException, CacheException, IOException {
		Long id=new Long(req.getParameter("id"));
		int year=Integer.parseInt(req.getParameter("year"));
		int month=Integer.parseInt(req.getParameter("month"));
		String chartCacheId;
		chartCacheId="MonthlyChartChart_"+id+"_"+Integer.toString(year)+"_"+Integer.toString(month)+req.getLocale(); //$NON-NLS-1$
		Cache chartCache=cacheManager.getCache("chartCache"); //$NON-NLS-1$
		Element element=chartCache.get(chartCacheId);
		if(element!=null)
		{
			res.setContentType("image/png"); //$NON-NLS-1$
			res.getOutputStream().write((byte[]) element.getValue());			
		}
		else
		{
			Session session=HibernateUtil.getSession();
			Station station=(Station) session.get(Station.class,id);
			List records=StationUtils.getSummaryMonth(station,year, month);
			JFreeChart chart=null;
			chart=ChartFactory.createBarChart(null,null,null,null,PlotOrientation.VERTICAL,true,true,false);
			chart.setTitle(TranslationUtils.getString("monthly_chart_for",req.getLocale())+" "+station.getTitle());
			CategoryPlot plot=chart.getCategoryPlot();
			BarRenderer renderer=(BarRenderer) plot.getRenderer();
			renderer.setSeriesPaint(0,Color.BLUE);
			renderer.setSeriesPaint(1,Color.RED);
			renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
			renderer.setItemLabelsVisible(true);
			renderer.setItemMargin(0.3);
			DefaultCategoryDataset minMaxData=new DefaultCategoryDataset();
			DefaultCategoryDataset avgData=new DefaultCategoryDataset();
			Iterator it=records.iterator();
			DateTool dateTool=new DateTool();
			while(it.hasNext())
			{
				Object[] row=(Object[]) it.next();
				Date stamp=(Date) row[0];
				Float min=(Float) row[2]; 
				Float max=(Float) row[3];
				Float avg=(Float) row[4]; 
				String date=dateTool.format("dd",stamp);
				minMaxData.addValue(min,TranslationUtils.getString("min",req.getLocale()),date);			
				minMaxData.addValue(max,TranslationUtils.getString("max",req.getLocale()),date);			
				avgData.addValue(avg,TranslationUtils.getString("average",req.getLocale()),date);			
			}
			plot.setDataset(0,minMaxData);
			plot.setDataset(1,avgData);
			LineAndShapeRenderer lineRenderer=new LineAndShapeRenderer();
			lineRenderer.setPaint(Color.lightGray);
			plot.setRenderer(1,lineRenderer);
			try {
				res.setContentType("image/png"); //$NON-NLS-1$
				byte[] image=ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) (1024),(int) (768)));
				chartCache.put(new Element(chartCacheId, image));;
				res.getOutputStream().write(image);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		return null;
	}
	
		
	private Template doChart(HttpServletRequest req, HttpServletResponse res) throws HibernateException, IllegalStateException, CacheException, IOException {
		// Fetch parameters
		Long id=new Long(req.getParameter("id")); // Station ID //$NON-NLS-1$
		boolean reverse=(req.getParameter("reverse")==null); //$NON-NLS-1$
		boolean notime=true;
		int delta=24;
		float scale=1;
		{
			String scaleParam=req.getParameter("scale");
			if (scaleParam!=null)
			{
				try
				{
					scale=Math.max(0,Math.min(1,Float.parseFloat(scaleParam)));
				}
				catch(NumberFormatException e)
				{				
				}
			}
		}
		Calendar calendar=parseDateParameters(req,false);
		{
			String str;
			str=req.getParameter("delta"); //$NON-NLS-1$
			if (str!=null)
			{				
				delta=Integer.parseInt(str);
			}
			str=req.getParameter("year"); //$NON-NLS-1$
			if (str!=null)
			{				
				notime=false;
			}
			// If no time was given, use reverse mode in any case
			reverse=reverse || notime;
		}
		Date start,end;
		if (!reverse)
		{
			start=calendar.getTime();
			calendar.add(Calendar.HOUR,delta);
			end=calendar.getTime();
		}
		else
		{
			end=calendar.getTime();
			calendar.add(Calendar.HOUR,-delta);
			start=calendar.getTime();						
		}
		String chartCacheId;
		if (!notime)
		{
			chartCacheId="ThermalRHChart"+id+"_"+Float.toString(scale)+start.toString()+end.toString()+req.getLocale();; //$NON-NLS-1$
		}
		else
		{
			chartCacheId="ThermalRHChartNOW"+id+"_"+Float.toString(scale)+Integer.toString(delta)+req.getLocale();;			 //$NON-NLS-1$
		}
		log.debug("Checking cache for" + chartCacheId);					 //$NON-NLS-1$
		Cache chartCache=cacheManager.getCache("chartCache"); //$NON-NLS-1$
		Element element=chartCache.get(chartCacheId);
		if(element!=null)
		{
			log.debug("Found cache entry for" + chartCacheId);					 //$NON-NLS-1$
			res.setContentType("image/png"); //$NON-NLS-1$
			res.getOutputStream().write((byte[]) element.getValue());			
		}
		else
		{
			Session session=HibernateUtil.getSession();
			Station station=(Station) session.get(Station.class,id);
			JFreeChart chart=null;
			List records=StationUtils.getTimeRangeStationRecords(station,start,end);
			StationRecord min=StationUtils.getMinTimeRangeStationRecord(station,start,end);
			StationRecord max=StationUtils.getMaxTimeRangeStationRecord(station,start,end);
			StationRecordsXYDataset dataset1=new StationRecordsXYDataset(station,records,new int[]{StationRecordsXYDataset.DATA_TEMPERATURE});		
			StationRecordsXYDataset dataset2=new StationRecordsXYDataset(station,records,new int[]{StationRecordsXYDataset.DATA_HUMADITY});		
			chart=ChartFactory.createTimeSeriesChart(TranslationUtils.getString("weather_station_records_for",req.getLocale())+" "+station.getTitle(),null,null,null,false,true,false); //$NON-NLS-1$
			chart.getXYPlot().setDataset(1,dataset1);
			chart.getXYPlot().setDataset(2,dataset2);
			chart.getXYPlot().setRenderer(2,new StandardXYItemRenderer());
			chart.getXYPlot().getRenderer(2).setPaint(Color.BLUE);
			chart.getXYPlot().getRenderer(2).setStroke(new BasicStroke((float) 0.1));
			chart.getXYPlot().setRangeAxis(0,new NumberAxis(TranslationUtils.getString("temperatures",req.getLocale()))); 
			chart.getXYPlot().setRangeAxis(1,new NumberAxis(TranslationUtils.getString("relative_humadity",req.getLocale())));
			chart.getXYPlot().mapDatasetToRangeAxis(1,0);
			chart.getXYPlot().mapDatasetToRangeAxis(2,1);
			chart.getXYPlot().getRangeAxis(0).setRangeWithMargins(min.getTemperature(),max.getTemperature());
			chart.getXYPlot().getRangeAxis(1).setRangeWithMargins(min.getHumadity(),max.getHumadity());
			try {
				res.setContentType("image/png"); //$NON-NLS-1$
				byte[] image=ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) (800*scale),(int) (600*scale)));
				chartCache.put(new Element(chartCacheId, image));;
				log.debug("Storing cache entry for" + chartCacheId);					 //$NON-NLS-1$
				res.getOutputStream().write(image);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return null;
	}
	
	private Template doTop(HttpServletRequest req, HttpServletResponse res, Context context) throws ParseErrorException, Exception, Exception {
		
		List stations = StationUtils.getAllStations(true);
		context.put("stations",stations); //$NON-NLS-1$
		return getTemplate(req,"top"); //$NON-NLS-1$
	}
	
	private Calendar parseDateParameters(HttpServletRequest req,boolean zerotime)
	{
		Calendar calendar=Calendar.getInstance(req.getLocale());
		if (zerotime)
		{
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
		}
		{
			String str;
			String[] params=new String[]{"year","month","day"}; 
			int[] fields=new int[]{Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH};
			for(int i=0;i<params.length;++i)
			{
				try
				{
					str=req.getParameter(params[i]);
					if (str!=null)
					{
						int val=Integer.parseInt(str);
						if(fields[i]==Calendar.MONTH)
							--val;						
						calendar.set(fields[i],val);
					}					
				}
				catch(NumberFormatException e)
				{
					log.debug("Bad number input",e);					 //$NON-NLS-1$
				}
			}
		}
		return calendar;
	}
	
	private Template doOverlook(HttpServletRequest req, HttpServletResponse res, Context context) throws ResourceNotFoundException, ParseErrorException, Exception
	{
		Long id=new Long(req.getParameter("id")); 
		Session session=HibernateUtil.getSession();
		Station station=(Station) session.get(Station.class,id);
		List records=StationUtils.getOverlook(station);
		context.put("station",station); //$NON-NLS-1$
		context.put("records",records); //$NON-NLS-1$
		return getTemplate(req,"overlook"); //$NON-NLS-1$
		
	}
	
	private Template doViewYear(HttpServletRequest req, HttpServletResponse res, Context context) throws ResourceNotFoundException, ParseErrorException, Exception {
		Long id=new Long(req.getParameter("id"));
		int year=Integer.parseInt(req.getParameter("year"));
		Session session=HibernateUtil.getSession();
		Station station=(Station) session.get(Station.class,id);
		List records=StationUtils.getSummaryYear(station,year);
		context.put("year",new Integer(year));
		context.put("station",station); //$NON-NLS-1$
		context.put("records",records); //$NON-NLS-1$
		return getTemplate(req,"view_year"); //$NON-NLS-1$
	}

	private Template doViewMonth(HttpServletRequest req, HttpServletResponse res, Context context) throws ResourceNotFoundException, ParseErrorException, Exception {
		Long id=new Long(req.getParameter("id"));
		int year=Integer.parseInt(req.getParameter("year"));
		int month=Integer.parseInt(req.getParameter("month"));
		Session session=HibernateUtil.getSession();
		Station station=(Station) session.get(Station.class,id);
		List records=StationUtils.getSummaryMonth(station,year, month);
		context.put("year",new Integer(year));
		context.put("month",new Integer(month));
		context.put("station",station); //$NON-NLS-1$
		context.put("records",records); //$NON-NLS-1$
		return getTemplate(req,"view_month"); //$NON-NLS-1$
	}
	
	private Template doViewDay(HttpServletRequest req, HttpServletResponse res, Context context) throws ResourceNotFoundException, ParseErrorException, Exception {
		Long id=new Long(req.getParameter("id")); 
		Calendar calendar=parseDateParameters(req,true);
		Date start,end;
		start=calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR,1);
		end=calendar.getTime();
		Session session=HibernateUtil.getSession();
		Station station=(Station) session.get(Station.class,id);
		context.put("station",station); //$NON-NLS-1$
		context.put("currDate",start);
		context.put("nextDate",end);
		calendar.add(Calendar.DAY_OF_YEAR,-2);
		context.put("prevDate",calendar.getTime());
		
		List records=StationUtils.getTimeRangeStationRecords(station,start,end);
		StationRecord maxData=StationUtils.getMaxTimeRangeStationRecord(station,start,end);
		StationRecord minData=StationUtils.getMinTimeRangeStationRecord(station,start,end);
		context.put("records",records); 
		context.put("maxData",maxData); 
		context.put("minData",minData); 
		return getTemplate(req,"view"); //$NON-NLS-1$
	}
	
	public Template getTemplate(HttpServletRequest req, String tmpl) throws ResourceNotFoundException, ParseErrorException, Exception {
		return super.getTemplate(tmpl+TEMPLATE_EXT);
	}
	
	protected Properties loadConfiguration(ServletConfig conf)
	{
		Properties props=new Properties();
		try {
			props.load(WeatherServlet.class.getResourceAsStream("/velocity.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
		
	}
	
	public Template handleRequest(HttpServletRequest req,HttpServletResponse res,Context context) throws ResourceNotFoundException, ParseErrorException, Exception
	{
		// Set cache expire
		res.setDateHeader("Expires",System.currentTimeMillis() + 60*60*1000);
		// Build perfered languages list(Enumeration sucks!)		
		List locales=new ArrayList();
		Enumeration e=req.getLocales();
		while(e.hasMoreElements())
			locales.add(e.nextElement());
		// Set translation object
		context.put("messages",TranslationUtils.getInstance());
		context.put("list",listTool);
		context.put("date",dateTool);
		context.put("math",mathTool);
		context.put("number",numberTool);
		context.put("locales",locales);
		context.put("locale",locales.get(0));
		// Date time formatter
		context.put("dateTimeFormatter",DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,req.getLocale())); 
		context.put("dateFormatter",DateFormat.getDateInstance(DateFormat.SHORT,req.getLocale()));
		// Site title
		context.put("title",TranslationUtils.getString("weather_stations",locales)); //$NON-NLS-1$ //$NON-NLS-2$
		// Split per operation
		String operation=req.getPathInfo();
		if (operation==null)
			operation="/top"; //$NON-NLS-1$
		if (operation.equals("/")) //$NON-NLS-1$
			operation="/top"; //$NON-NLS-1$
		Template template=null;
		if (operation.equals("/top")) //$NON-NLS-1$
		{
			template=doTop(req,res,context);
		}
		else if (operation.equals("/overlook")) //$NON-NLS-1$
		{			
			template=doOverlook(req,res,context);
		}
		else if (operation.equals("/view_year")) //$NON-NLS-1$
		{			
			template=doViewYear(req,res,context);
		}
		else if (operation.equals("/view_month")) //$NON-NLS-1$
		{			
			template=doViewMonth(req,res,context);
		}
		else if (operation.equals("/view_day")) //$NON-NLS-1$
		{			
			template=doViewDay(req,res,context);
		}
		else if (operation.equals("/chart")) //$NON-NLS-1$
		{			
			template=doChart(req,res);
		}
		else if (operation.equals("/monthly_chart")) //$NON-NLS-1$
		{			
			template=doMonthlyChart(req,res);
		}
		else
		{
		}
		HibernateUtil.closeSession();
		return template;
	}


	public void init()
	{		
		try {
			initConfig();
			initLog();
			initCache();
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	private void initCache() throws CacheException {
		URL url = getClass().getResource("/ehcache.xml"); //$NON-NLS-1$
		cacheManager=CacheManager.create(url);
	}

	private void initConfig() {
		// Currently a stub only.		
	}

		
	private void initLog() {
		// TODO: Find out why we have to deal with Log4JLogger directly..
		log=LogFactory.getLog(WeatherServlet.class);
	}

	protected void setContentType(HttpServletRequest req, HttpServletResponse res) {
		String accept=req.getHeader("Accept"); //$NON-NLS-1$
		if (accept.indexOf(XHTML_MIME)!=-1)
		{
			res.setContentType(HTML_MIME);
		}
		else
		{
			res.setContentType(HTML_MIME);			
		}
	}
	
	
	
}
