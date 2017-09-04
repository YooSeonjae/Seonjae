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

   public String get() { // 현재 상영작 크롤링
      System.setProperty("phantomjs.binary.path",
            "/Users/NOTE/workspace/[WP]_02_term_201302434/WebContent/phantomjs-2.1.1-windows/bin/phantomjs.exe");

      WebDriver driver = new PhantomJSDriver();

      String url = "https://www.naver.com"; //네이버에서 크롤링
      String data;

      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

      driver.get(url);

      driver.findElement(By.id("query")).sendKeys("영화"); //영화라고 쓰기
      driver.findElement(By.id("search_btn")).click(); // 검색버튼 누르기

      data = driver.getPageSource(); 

      Document document = Jsoup.parse(data); //데이터 파싱

      Elements elements = document.select(".thumb_list > li"); //파싱한 부분 선택

      List<String> list = new ArrayList<String>();

      for (Element e : elements) {
         list.add(e.html());
      }

      Map<String, Object> result = new HashMap<String, Object>();//크롤링해서 리스트에 저장
      result.put("list", list);

      JSONObject obj = new JSONObject(result);

      driver.quit();

      return obj.toJSONString();
   }
   public String afterget() { // 상영 예정작 크롤링
	      System.setProperty("phantomjs.binary.path",
	            "/Users/NOTE/workspace/[WP]_02_term_201302434/WebContent/phantomjs-2.1.1-windows/bin/phantomjs.exe");

	      WebDriver driver = new PhantomJSDriver();

	      String url = "https://www.naver.com"; //네이버에서 크롤링
	      String data;

	      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

	      driver.get(url);

	      driver.findElement(By.id("query")).sendKeys("상영예정작"); //상영예정작이라고 쓰기
	      driver.findElement(By.id("search_btn")).click(); // 검색버튼 누르기

	      data = driver.getPageSource();

	      Document document = Jsoup.parse(data);

	      Elements elements = document.select(".thumb_list > li"); //파싱한 부분 선택

	      List<String> list = new ArrayList<String>();

	      for (Element e : elements) {
	         list.add(e.html());
	      }

	      Map<String, Object> result = new HashMap<String, Object>();//크롤링해서 리스트에 저장
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

      HashMap<String, String> cgvMap = new HashMap<>(); //cgv의 각 영화관마다의 코드 저장
      cgvMap.put("대전", "0007");
      cgvMap.put("대전가오", "0154");
      cgvMap.put("대전탄방", "0202");
      cgvMap.put("대전터미널", "0127");
      cgvMap.put("유성노은", "0206");
      cgvMap.put("유성온천", "0209");
      String cgvCode = "";
      String cgvUrl = "";

      HashMap<String, String> lotteMap = new HashMap<>();//롯데시네마의 각 영화관마다의 코드 저장
      lotteMap.put("대전", "4002");
      lotteMap.put("대전둔산", "4006");
      String lotteCode = "";
      String lotteUrl = "";

      HashMap<String, String> megaMap = new HashMap<>(); //메가박스의 각 영화관마다의 코드 저장
      megaMap.put("대전", "3021");
      String megaCode = "";
      String megaUrl = "";

      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

      String[] t = theater.split(" ");
      if (t[0].equals("CGV")) {
         cgvCode = cgvMap.get((String) t[1]);
         cgvUrl = "http://www.cgv.co.kr/reserve/show-times/?areacode=03%2C205&theaterCode=" + cgvCode + "&date=2017"
               + date;  //cgv에서 크롤링

         driver.get(cgvUrl);

         String data;

         driver.switchTo().defaultContent();
         driver.switchTo().frame("ifrm_movie_time_table");

         data = driver.getPageSource();

         Document document = Jsoup.parse(data);//데이터 파싱

         Elements elements = document.select(".sect-showtimes");//파싱한 부분 선택

         for (Element e : elements) {
            result = e.html();
         }

         driver.quit();
      } else if (t[0].equals("롯데시네마")) {
         lotteCode = lotteMap.get((String) t[1]);
         lotteUrl = "http://www.lottecinema.co.kr/LCHS/Contents/Cinema/Cinema-Detail.aspx?divisionCode=1&detailDivisionCode=3&cinemaID="
               + lotteCode; //롯데시네마에서 크롤링

         driver.get(lotteUrl);

         String data;

         driver.findElement(By.id(formatting(date))).click();

         data = driver.getPageSource();

         Document document = Jsoup.parse(data); //데이터 파싱
         Elements elements = document.select(".time_inner");//파싱한 부분 선택

         for (Element e : elements) {
            result = e.html();
         }

         driver.quit();
      } else if (t[0].equals("메가박스")) {
         megaCode = megaMap.get((String) t[1]);
         megaUrl = "https://www.naver.com"; //네이버에서 파싱
         String data;
         
         driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
         driver.get(megaUrl);
        
         driver.findElement(By.id("query")).sendKeys("메가박스대전"); //네이버에서 검색
         driver.findElement(By.id("search_btn")).click();
         
         data = driver.getPageSource();
         
         Document document = Jsoup.parse(data);

         Elements elements = document.select(".list_tbl_box"); //파싱한 부분 선택

         for (Element e : elements) {
            result = e.html();
         }
         
         driver.quit();
      }

      return result;
   }

   private String formatting(String date) { //수를 월로 바꿔 롯데시네마 형식에 맞춰줌
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