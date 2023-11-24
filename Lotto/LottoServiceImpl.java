package hello.hellospring.lotto;

import java.util.List;

public class LottoServiceImpl implements LottoService{

    private final LottoRepository lottoRepository = new LottoRepositoryImpl();

    @Override
    public Lotto join(Lotto lotto) {
        return lottoRepository.join(lotto);
    }

    @Override
    public void numbersave(Long id, int[] lottonumber) {
        lottoRepository.numbersave(id, lottonumber);
    }

    @Override
    public Long findUserId(String userId, String password) {
        return lottoRepository.findUserId(userId, password);
    }

    @Override
    public int[] createLotto() {
        return lottoRepository.createLotto();
    }

    @Override
    public String generateColor(int number) {
        return lottoRepository.generateColor(number);
    }

    @Override
    public int[] mylottonumber(Long id) {
        return lottoRepository.mylottonumber(id);
    }

    @Override
    public String checkLottoRank(int[] originalnumber, int[] mynumber) {
        return lottoRepository.checkLottoRank(originalnumber, mynumber);
    }

    @Override
    public Lotto findById(Long id) {
        return lottoRepository.findById(id);
    }

    @Override
    public int findMyRank(String myrank) {
        return lottoRepository.findMyRank(myrank);
    }

    @Override
    public void lottoBuyDateTime(Long id) {
        lottoRepository.lottoBuyDateTime(id);
    }

    @Override
    public List<Lotto> findAll() {
        return lottoRepository.findAll();
    }

}
