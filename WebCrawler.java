package seon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
//WebCrawler.java
public class WebCrawler {

   public String get() { // ���� ���� ũ�Ѹ�
      System.setProperty("phantomjs.binary.path",
            "/Users/NOTE/workspace/[WP]_02_term_201302434/WebContent/phantomjs-2.1.1-windows/bin/phantomjs.exe");

      WebDriver driver = new PhantomJSDriver();

      String url = "https://www.naver.com"; //���̹����� ũ�Ѹ�
      String data;

      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

      driver.get(url);

      driver.findElement(By.id("query")).sendKeys("��ȭ"); //��ȭ��� ����
      driver.findElement(By.id("search_btn")).click(); // �˻���ư ������

      data = driver.getPageSource(); 

      Document document = Jsoup.parse(data); //������ �Ľ�

      Elements elements = document.select(".thumb_list > li"); //�Ľ��� �κ� ����

      List<String> list = new ArrayList<String>();

      for (Element e : elements) {
         list.add(e.html());
      }

      Map<String, Object> result = new HashMap<String, Object>();//ũ�Ѹ��ؼ� ����Ʈ�� ����
      result.put("list", list);

      JSONObject obj = new JSONObject(result);

      driver.quit();

      return obj.toJSONString();
   }
   public String afterget() { // �� ������ ũ�Ѹ�
	      System.setProperty("phantomjs.binary.path",
	            "/Users/NOTE/workspace/[WP]_02_term_201302434/WebContent/phantomjs-2.1.1-windows/bin/phantomjs.exe");

	      WebDriver driver = new PhantomJSDriver();

	      String url = "https://www.naver.com"; //���̹����� ũ�Ѹ�
	      String data;

	      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

	      driver.get(url);

	      driver.findElement(By.id("query")).sendKeys("�󿵿�����"); //�󿵿������̶�� ����
	      driver.findElement(By.id("search_btn")).click(); // �˻���ư ������

	      data = driver.getPageSource();

	      Document document = Jsoup.parse(data);

	      Elements elements = document.select(".thumb_list > li"); //�Ľ��� �κ� ����

	      List<String> list = new ArrayList<String>();

	      for (Element e : elements) {
	         list.add(e.html());
	      }

	      Map<String, Object> result = new HashMap<String, Object>();//ũ�Ѹ��ؼ� ����Ʈ�� ����
	      result.put("list", list);

	      JSONObject obj = new JSONObject(result);

	      driver.quit();

	      return obj.toJSONString();
	   }

   public String getTimeTable(String date, String theater) {
      System.setProperty("phantomjs.binary.path",
            "/Users/NOTE/workspace/[WP]_02_term_201302434/WebContent/phantomjs-2.1.1-windows/bin/phantomjs.exe");
      WebDriver driver = new PhantomJSDriver();
      String result = "";

      HashMap<String, String> cgvMap = new HashMap<>(); //cgv�� �� ��ȭ�������� �ڵ� ����
      cgvMap.put("����", "0007");
      cgvMap.put("��������", "0154");
      cgvMap.put("����ź��", "0202");
      cgvMap.put("�����͹̳�", "0127");
      cgvMap.put("��������", "0206");
      cgvMap.put("������õ", "0209");
      String cgvCode = "";
      String cgvUrl = "";

      HashMap<String, String> lotteMap = new HashMap<>();//�Ե��ó׸��� �� ��ȭ�������� �ڵ� ����
      lotteMap.put("����", "4002");
      lotteMap.put("�����л�", "4006");
      String lotteCode = "";
      String lotteUrl = "";

      HashMap<String, String> megaMap = new HashMap<>(); //�ް��ڽ��� �� ��ȭ�������� �ڵ� ����
      megaMap.put("����", "3021");
      String megaCode = "";
      String megaUrl = "";

      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

      String[] t = theater.split(" ");
      if (t[0].equals("CGV")) {
         cgvCode = cgvMap.get((String) t[1]);
         cgvUrl = "http://www.cgv.co.kr/reserve/show-times/?areacode=03%2C205&theaterCode=" + cgvCode + "&date=2017"
               + date;  //cgv���� ũ�Ѹ�

         driver.get(cgvUrl);

         String data;

         driver.switchTo().defaultContent();
         driver.switchTo().frame("ifrm_movie_time_table");

         data = driver.getPageSource();

         Document document = Jsoup.parse(data);//������ �Ľ�

         Elements elements = document.select(".sect-showtimes");//�Ľ��� �κ� ����

         for (Element e : elements) {
            result = e.html();
         }

         driver.quit();
      } else if (t[0].equals("�Ե��ó׸�")) {
         lotteCode = lotteMap.get((String) t[1]);
         lotteUrl = "http://www.lottecinema.co.kr/LCHS/Contents/Cinema/Cinema-Detail.aspx?divisionCode=1&detailDivisionCode=3&cinemaID="
               + lotteCode; //�Ե��ó׸����� ũ�Ѹ�

         driver.get(lotteUrl);

         String data;

         driver.findElement(By.id(formatting(date))).click();

         data = driver.getPageSource();

         Document document = Jsoup.parse(data); //������ �Ľ�
         Elements elements = document.select(".time_inner");//�Ľ��� �κ� ����

         for (Element e : elements) {
            result = e.html();
         }

         driver.quit();
      } else if (t[0].equals("�ް��ڽ�")) {
         megaCode = megaMap.get((String) t[1]);
         megaUrl = "https://www.naver.com"; //���̹����� �Ľ�
         String data;
         
         driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
         driver.get(megaUrl);
        
         driver.findElement(By.id("query")).sendKeys("�ް��ڽ�����"); //���̹����� �˻�
         driver.findElement(By.id("search_btn")).click();
         
         data = driver.getPageSource();
         
         Document document = Jsoup.parse(data);

         Elements elements = document.select(".list_tbl_box"); //�Ľ��� �κ� ����

         for (Element e : elements) {
            result = e.html();
         }
         
         driver.quit();
      }

      return result;
   }

   private String formatting(String date) { //���� ���� �ٲ� �Ե��ó׸� ���Ŀ� ������
      String month = date.substring(0, 2);
      String m;
      switch (month) {
      case "01":
         m = "January";
         break;
      case "02":
         m = "February";
         break;
      case "03":
         m = "March";
         break;
      case "04":
         m = "April";
         break;
      case "05":
         m = "May";
         break;
      case "06":
         m = "June";
         break;
      case "07":
         m = "July";
         break;
      case "08":
         m = "August";
         break;
      case "09":
         m = "September";
         break;
      case "10":
         m = "October";
         break;
      case "11":
         m = "November";
         break;
      case "12":
         m = "December";
         break;

      default:
         m = "";
         break;
      }

      return m + date.substring(2, 4);
   }
}