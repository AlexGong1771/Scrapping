package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class SearchController {

    @PostMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        try {
            String url = "https://duckduckgo.com/html/?q=" + query;
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .get();

            Elements links = document.select(".result__a");
            StringBuilder sb = new StringBuilder();

            System.out.println("🔗 Nájdené odkazy:");

            for (Element link : links) {
                String rawLink = link.attr("href");
                if (rawLink.contains("uddg=")) {
                    String encodedUrl = rawLink.split("uddg=")[1].split("&")[0];
                    String decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
                    sb.append(decodedUrl).append("\n");
                }
            }

            // Uloženie do súboru
            Files.writeString(Paths.get("C:\\Users\\42194\\Downloads\\demo (1)\\demo\\src\\main\\resources\\static\\link.txt"), sb);

            model.addAttribute("query", query);
            model.addAttribute("links", sb.toString().split("\n"));

        } catch (Exception e) {
            model.addAttribute("error", "❌ Nastala chyba: " + e.getMessage());
        }

        return "result"; // HTML template name
    }
}

