package hello.hellospring.lotto;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Lotto
{
    private Long id;

    // 회원가입
    private String userId;
    private String password;
    private String name;
    private int age;
    private String email;


    // 당첨등수
    private String grade;

    // 로또 번호
    private int[] lottonumber = new int[5];

    // 회원가입 날짜
    private LocalDateTime localDateTime;

    // 로또 구매 날짜
    private LocalDateTime lottobuyDateTime;

    // 당첨카운트
    private int lottorank1;
    private int lottorank2;
    private int lottorank3;
    private int lottorank4;
    private int lottorank5;
    private int lottorank6;
    private int lottorank7;


    public Lotto()
    {

    }

    public Lotto(String userId, String password, String name, int age, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int[] getLottonumber() {
        return lottonumber;
    }

    public void setLottonumber(int[] lottonumber) {
        this.lottonumber = lottonumber;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLottobuyDateTime() {
        return lottobuyDateTime;
    }

    public void setLottobuyDateTime(LocalDateTime lottobuyDateTime) {
        this.lottobuyDateTime = lottobuyDateTime;
    }

    public int getLottorank1() {
        return lottorank1;
    }

    public void setLottorank1(int lottorank1) {
        this.lottorank1 = lottorank1;
    }

    public int getLottorank2() {
        return lottorank2;
    }

    public void setLottorank2(int lottorank2) {
        this.lottorank2 = lottorank2;
    }

    public int getLottorank3() {
        return lottorank3;
    }

    public void setLottorank3(int lottorank3) {
        this.lottorank3 = lottorank3;
    }

    public int getLottorank4() {
        return lottorank4;
    }

    public void setLottorank4(int lottorank4) {
        this.lottorank4 = lottorank4;
    }

    public int getLottorank5() {
        return lottorank5;
    }

    public void setLottorank5(int lottorank5) {
        this.lottorank5 = lottorank5;
    }

    public int getLottorank6() {
        return lottorank6;
    }

    public void setLottorank6(int lottorank6) {
        this.lottorank6 = lottorank6;
    }

    public int getLottorank7() {
        return lottorank7;
    }

    public void setLottorank7(int lottorank7) {
        this.lottorank7 = lottorank7;
    }

    @Override
    public String toString() {
        return "Lotto{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", grade='" + grade + '\'' +
                ", lottonumber=" + Arrays.toString(lottonumber) +
                ", localDateTime=" + localDateTime +
                ", lottobuyDateTime=" + lottobuyDateTime +
                '}';
    }
}
