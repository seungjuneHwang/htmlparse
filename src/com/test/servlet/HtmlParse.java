package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/**
 * Servlet implementation class HtmlParse
 */
@WebServlet(name = "parse", urlPatterns = { "/parse" })
public class HtmlParse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 파서에 대한 변수
	private String source_id;       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public HtmlParse() {
		super();
   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	// 인터넷에서 가지고 온 소스 복붙
	public String GetNumber() throws Exception{
		Document doc = Jsoup.connect("http://movie.naver.com/movie/running/current.nhn").get();
		Elements contents;
		
		StringBuffer sb = new StringBuffer();
		
		Elements img = doc.select("div.thumb img[src]");
		int idx = 0;
		for (Element eTimg : img) {
			if (idx++ == 10) {
				break;
			}
			String imgSrc = eTimg.attr("src");
			String tit = eTimg.attr("alt");
			String [] img1 = imgSrc.split("\\?");
			System.out.println(img1[0]);
			System.out.println(tit);
		}

//		contents = doc.select("#bnusNo");
//		String bNum = "[B] : "+contents.attr("alt");
//		System.out.println("[B] : "+contents.attr("alt"));
//		sb.append();
		return sb.toString();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Document doc = Jsoup.connect("http://movie.naver.com/movie/running/current.nhn").get();
		Elements contents;
		
		StringBuffer sb = new StringBuffer();
		ArrayList<MovieInfo> list = new ArrayList<>();
		Elements links = doc.select("div.thumb a[href]");
		Elements img = doc.select("div.thumb img[src]");
		int idx = 0;
		ArrayList<String> linkList = new ArrayList<>();
		for (Element link : links) {
			linkList.add("http://movie.naver.com" + link.attr("href"));
			//System.out.println(link.attr("href"));
		}
		
		for (Element eTimg : img) {
			String href = eTimg.attr("src");
			String imgSrc = eTimg.attr("src");
			String tit = eTimg.attr("alt");
			String [] img1 = imgSrc.split("\\?");
			sb.append("img 주소: " + img1[0] + ", ");
			System.out.println(href);
//			System.out.println(img1[0]);
//			System.out.println(tit);

			sb.append("타이틀: " + tit + ", ");
			MovieInfo bean = new MovieInfo();
			bean.setImg(img1[0]);
			bean.setTitle(tit);
			bean.setLink(linkList.get(idx++));
			list.add(bean);
		}
		System.out.println(linkList.size());
		System.out.println(list.size());
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String str = gson.toJson(list);
		out.print(str);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	class MovieInfo {
		private String img;
		private String title;
		private String link;
		
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}

}
