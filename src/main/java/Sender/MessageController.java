package Sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {
    private final OrderMessageSender orderMessageSender;

    @Autowired
    public MessageController(OrderMessageSender orderMessageSender) {
        this.orderMessageSender = orderMessageSender;
    }
    @RequestMapping(method = RequestMethod.GET , value="/add")
    public ModelAndView addBankRequest() {
        ModelAndView mav = new ModelAndView("bb");
        mav.addObject("check",new Chaque());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST , value="/add")
    public void addNewBank(@ModelAttribute Chaque check, HttpServletResponse response) throws IOException {
        orderMessageSender.sendOrder(check);
        System.out.println(check.FirstBank);
    }
    @PostMapping("/SendingCheck")
    public void handleMessage(@RequestBody Chaque check, RedirectAttributes redirectAttributes) {
        orderMessageSender.sendOrder(check);
        System.out.println(check.FirstBank);
        redirectAttributes.addFlashAttribute("message", "Order message sent successfully");

    }
}
