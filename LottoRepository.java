package hello.hellospring.lotto;

import java.util.List;

public interface LottoRepository {
    Lotto join(Lotto lotto);
    void numbersave(Long id, int[] lottonumber);
    Long findUserId(String userId, String password);
    int[] createLotto();
    String generateColor(int number);
    int[] mylottonumber(Long id);
    String checkLottoRank(int[] originalnumber, int[] mynumber);
    Lotto findById(Long id);
    int findMyRank(String myrank);
    void lottoBuyDateTime(Long id);
    List<Lotto> findAll();
}
