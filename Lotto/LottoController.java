package hello.hellospring.lottocontroller;

import hello.hellospring.lotto.Lotto;
import hello.hellospring.lotto.LottoService;
import hello.hellospring.lotto.LottoServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class LottoController {
    LottoService lottoService = new LottoServiceImpl();
    private Long finduserId;
    private String loginId;

    // 회원가입 로그인
    @GetMapping("/lottoindex")
    public String LottoJoin()
    {
        return "/lotto/lottoindex";
    }

    @GetMapping("/lottosignup")
    public String LottoSignup()
    {
        return "/lotto/lottosignup";
    }

    @PostMapping("/lottosigncomplete")
    public String LottoSigncomplete(@RequestParam("userId") String userId,
                                    @RequestParam("password") String password,
                                    @RequestParam("name") String name,
                                    @RequestParam("age") int age,
                                    @RequestParam("email") String email)
    {
        Lotto lotto = new Lotto(userId, password, name, age, email);
        lottoService.join(lotto);
        return "redirect:/lottoindex";
    }

    @GetMapping("/lottologin")
    public String LottoLogin()
    {
        return "/lotto/lottologin";
    }

    @PostMapping("/lottologin")
    public String LottoLoginComplete(@RequestParam("userId") String userId,
                                     @RequestParam("password") String password,
                                     Model model)
    {
        finduserId = lottoService.findUserId(userId, password);

        if (finduserId != null)
        {
            System.out.println("로그인한 id(Long) = " + finduserId);
            loginId = userId;
            model.addAttribute("userId", loginId);
            return "redirect:/lottomain";
        }
        else
        {
            model.addAttribute("loginid", finduserId);
            model.addAttribute("loginFailed", "로그인 정보가 없습니다.");
            return "/lotto/lottologin";
        }

    }

    // 메인화면
    @GetMapping("/lottomain")
    public String Golottomain(Model model)
    {
        model.addAttribute("userId", loginId);
        int[] mylottonumber = lottoService.mylottonumber(finduserId);
        model.addAttribute("mylottonumber", mylottonumber);
        return "/lotto/lottomain";
    }

    // 로또 구매
    @GetMapping("/buylotto")
    public String BuyLotto(Model model)
    {
        model.addAttribute("userId", loginId);
        return "/lotto/lottobuy";
    }

    @PostMapping("/buylottocomplete")
    public String BuyLottoComplete(@RequestParam("numbers") int[] lottonumber,
                                   Model model)
    {
        lottoService.numbersave(finduserId, lottonumber);
        //lottoService.updateDateTime(finduserId);
        lottoService.lottoBuyDateTime(finduserId);
        return "redirect:/lottomain";
    }

    // 당첨여부
    @GetMapping("/checkwin")
    public String CheckWin(Model model)
    {
        int[] mylottonumber = lottoService.mylottonumber(finduserId);
        System.out.println("mylottonumber = " + Arrays.toString(mylottonumber));
        int[] lottonumbers = lottoService.createLotto();
        model.addAttribute("lottonumbers", lottonumbers);
        model.addAttribute("mylottonumber", mylottonumber);
        model.addAttribute("userId", loginId);
        String[] lottoColors = new String[mylottonumber.length];
        for (int i = 0; i < mylottonumber.length; i++)
        {
            lottoColors[i] = lottoService.generateColor(i);
            //System.out.println("lottoColors = " + lottoColors[i]);
            //System.out.println("i = " + i);
        }
        model.addAttribute("lottoColors", lottoColors);
        // model.addAttribute는 반복문 안에 있으면 안되고 밖에 있어야 한다
        String rank = lottoService.checkLottoRank(lottoService.createLotto(), mylottonumber);
        model.addAttribute("rank", rank);
        return "/lotto/lottowincheck";
    }

    // 당첨자 목록
    @GetMapping("/winpeople")
    public String WinPeople(Model model)
    {
        List<Lotto> lottoList = lottoService.findAll();
        int[] lottonumbers = lottoService.createLotto();
        String[] lottoColors = new String[lottonumbers.length];
        for (int i = 0; i < lottonumbers.length; i++)
        {
            lottoColors[i] = lottoService.generateColor(i);
            //System.out.println("lottoColors = " + lottoColors[i]);
            //System.out.println("i = " + i);
        }
        model.addAttribute("lottoColors", lottoColors);
        model.addAttribute("userId", loginId);
        model.addAttribute("lottoNumbers", lottonumbers);
        model.addAttribute("lottoList", lottoList);
        model.addAttribute("rankcount1", lottoList.stream().mapToInt(Lotto::getLottorank1).sum());
        model.addAttribute("rankcount2", lottoList.stream().mapToInt(Lotto::getLottorank2).sum());
        model.addAttribute("rankcount3", lottoList.stream().mapToInt(Lotto::getLottorank3).sum());
        model.addAttribute("rankcount4", lottoList.stream().mapToInt(Lotto::getLottorank4).sum());
        model.addAttribute("rankcount5", lottoList.stream().mapToInt(Lotto::getLottorank5).sum());
        model.addAttribute("rankcount6", lottoList.stream().mapToInt(Lotto::getLottorank6).sum());
        model.addAttribute("rankcount7", lottoList.stream().mapToInt(Lotto::getLottorank7).sum());
        // lottoList 리스트를 스트림 형태로 변환 후 Lotto 객체에서 getLottorank 메소드를 호출해서 해당 객체들의 필드 값을 가져온다
        // mapToint는 intStream으로 매핑해서 정수 형태로 변환한다
        // 그 후 sum() 함수로 불러온 데이터들의 합계를 계산한다
        return "/lotto/lottowinpeople";
    }
}
