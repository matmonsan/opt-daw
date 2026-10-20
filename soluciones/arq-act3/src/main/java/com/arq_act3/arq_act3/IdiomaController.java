package com.arq_act3.arq_act3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IdiomaController {

    @GetMapping("/elegir")
    public String elegir(@RequestParam(defaultValue = "english") String idioma) {
        if (idioma.equals("spanish")) {
            return "redirect:/spanish.html";
        } else if (idioma.equals("french")) {
            return "redirect:/french.html";
        } else if (idioma.equals("german")) {
            return "redirect:/german.html";
        } else {
            return "redirect:/english.html";
        }
    }
}
