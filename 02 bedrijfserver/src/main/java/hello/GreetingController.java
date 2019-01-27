//package hello;
//
//import Authentication.Authenticate;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class GreetingController {
//
//
//    String name;
//    String pass;
//
//    @GetMapping("/greeting")
//    public String greetingForm(Model model){
//        {
//            model.addAttribute("greeting", new Greeting());
//            return "greeting";
//
//        }
//    }
//
//
//
//    @PostMapping("/greeting")
//    public String greetingSubmit(@ModelAttribute Greeting greeting) {
//
//        name = greeting.getId();
//        pass = greeting.getContent();
//
//        Authenticate au = new Authenticate();
//        UserControls uc = new UserControls();
//        uc.login(name, pass);
//
//
//        if (au.loginState == true) {
//            return "redirect:result";
////                return "result";
//        }
//
//        else
//        {
//            System.out.println("cant connect");
//            return "greeting";
//        }
//
//    }
//
//
//}
