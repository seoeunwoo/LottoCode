package hello.hellospring.lotto;

import hello.hellospring.gugudan.Gugudan;
import hello.hellospring.studentconnection.DBConnectionUtility;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class LottoRepositoryImpl implements LottoRepository{
    private final Map<Long, Lotto> data = new HashMap<>();
    private Long id = 0L;
    private Long loginId;
    private Long findId;
    private int[] fixedLottoNumbers = null;
    private int rankcount1 = 0;
    private int rankcount2 = 0;
    private int rankcount3 = 0;
    private int rankcount4 = 0;
    private int rankcount5 = 0;
    private int rankcount6 = 0;
    private int rankcount7 = 0;
    private int fixedrankcount1;
    private int fixedrankcount2;
    private int fixedrankcount3;
    private int fixedrankcount4;
    private int fixedrankcount5;
    private int fixedrankcount6;
    private int fixedrankcount7;

    private Long getNextId()
    {
        String sql = "SELECT MAX(id) FROM lotto";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            if (resultSet.next())
            {
                Long maxId = resultSet.getLong(1);
                // 최대 ID 값에 1을 더한 값을 반환
                return maxId + 1;
            } else
            {
                // 테이블이 비어있을 경우, 1부터 시작
                return 1L;
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
            return null;
        }
    }
    private int[] newlottoNumbers()
    {
        int[] lotto = new int[6];
        Random random = new Random();

        for (int i = 0; i < lotto.length; i++)
        {
            lotto[i] = random.nextInt(45) + 1;
            for (int j = 0; j < i; j++)
            {
                if (lotto[i] == lotto[j])
                {
                    i--;
                    break;
                }
            }
        }
        // random함수를 이용해서 최소값 1 최대값 45의 로또 번호를 하나 생성
        // 2중 for문으로 i가 lotto 배열의 길이 값까지 하나씩 돌면서 번호를 랜덤으로 하나씩 생성함
        // j for문에서 i에 담긴 값과 일치하면 i에서 생성된 로또 번호를 삭제 후 break;
        // 중복되는 번호를 모두 삭제 하고 난 6개의 번호를 lotto 배열에 저장함

        Arrays.sort(lotto);
        // 저장 후 sort함수로 낮은 숫자부터 높은 숫자로 정렬함
        System.out.println("생성된 로또 번호 = " + Arrays.toString(lotto));
        fixedLottoNumbers = lotto;
        // 정렬 후 전역변수인 fixedLottoNumbers 변수에 lotto 배열 값을 다시 저장해서
        // 스프링 서버가 실행되는 동안 해당 번호가 유지되게 함
        return fixedLottoNumbers;
    }


    @Override
    public Lotto join(Lotto lotto) {
        Long nexdId = getNextId();

        lotto.setId(nexdId);
        data.put(nexdId, lotto);

        String sql = "insert into lotto(id, userid, password, name, age, email, fixedlottonumber, joindatetime) values (?, ?, ?, ?, ?, ?, ?, ?)";
        // update랑 마찬가지로 insert할때도 테이블 생성 후 필요한 데이터만 먼저 입력 하면 나머지는 자동으로 null 처리 된다

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, lotto.getId());
            preparedStatement.setString(2, lotto.getUserId());
            preparedStatement.setString(3, lotto.getPassword());
            preparedStatement.setString(4, lotto.getName());
            preparedStatement.setInt(5, lotto.getAge());
            preparedStatement.setString(6, lotto.getEmail());
            //preparedStatement.setString(7, lotto.getGrade());
            //String lottonumber = Arrays.toString(lotto.getLottonumber());
            //preparedStatement.setString(8, lottonumber);
            String fixedlottonumber = Arrays.toString(newlottoNumbers());
            preparedStatement.setString(7, fixedlottonumber);
            // 로또 번호를 int형 배열을 String형변환 후 db에 저장
            String localdatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            preparedStatement.setString(8, localdatetime);


            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            log.error("데이터 오류 입니다", e);
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
        return lotto;
    }

    @Override
    public void numbersave(Long id, int[] lottonumber) {
        Lotto lotto = data.get(id);
        lotto.setLottonumber(lottonumber);
        Arrays.sort(lottonumber);

        String sql = "update lotto set lottonumber = ? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            String mylottonumber = Arrays.toString(lotto.getLottonumber());

            preparedStatement.setString(1, mylottonumber);
            preparedStatement.setLong(2, lotto.getId());


            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("mylottonumber result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public Long findUserId(String userId, String password) {
        for(Map.Entry<Long, Lotto> entry : data.entrySet())
        {
            Lotto lotto = entry.getValue();
            if (lotto.getUserId().equals(userId) && lotto.getPassword().equals(password))
            {
                findId = entry.getKey();
                System.out.println("찾은 id = " + findId);
                return findId;
            }
        }
        // Map에 저장된 데이터를 기준으로 userid와 password가 일치하는 곳의 key 값인 id값을 가져와서 findId에 재할당
        // 일치하는 데이터가 없으면 null을 반환
        return null;
    }

    @Override
    public int[] createLotto() {
        return fixedLottoNumbers;
    }

    @Override
    public String generateColor(int number) {
        if (number == 0)
        {
            return "#e74c3c"; // 빨
        }
        else if (number == 1)
        {
            return "#f39c12"; // 주
        }
        else if (number == 2)
        {
            return "#2ecc71"; // 초
        }
        else if (number == 3)
        {
            return "#3498db"; // 파
        }
        else if (number == 4)
        {
            return "#29088A"; // 남
        }
        else if (number == 5)
        {
            return "#BE81F7"; // 보
        }
        else {
            return "#000000"; // 기본값은 검정색
        }
        // 컨트롤러에서 넘어온 배열 index 값을 number 매개변수에 담아서 들어온 값에 따라 색상코드값을 return해서 전달함
    }

    @Override
    public int[] mylottonumber(Long id) {
        Lotto lotto = data.get(id);
        int[] lottoNumbers = lotto.getLottonumber();
        System.out.println("내 로또 번호 = " + Arrays.toString(lottoNumbers));


        return lottoNumbers;
    }

    @Override
    public String checkLottoRank(int[] originalnumber, int[] mynumber) {
        int count = 0;
        for (int number : mynumber)
        {
            for (int original : originalnumber)
            {
                if (number == original)
                {
                    count++;
                    System.out.println("count = " + count);
                    break;
                }
            }
        }

        // 컨트롤러에서 입력한 로또 번호와 스프링서버 실행시 생성된 로또 번호값을 가져와서
        // 동일한 값이 있으면 count++로 개수를 체크함
        // 일치하는 숫자의 개수에 따라 등수 결정

        String rank;
        if (count == 6)
        {
            rank = "1등";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount1++;
                fixedrankcount1 = rankcount1;
                lotto.setLottorank1(fixedrankcount1);
            }
            else
            {
                rankcount1 = fixedrankcount1;
                lotto.setLottorank1(fixedrankcount1);
            }

            // Map에 grade가 null 이면 1등 값을 + 1 하고 현재 증가한 값을 전역변수인 fixedrankcount1 값에 저장함
            // null이 아니면 1등 값은 기존에 저장한 fixedrankcount1 값을 그대로 유지해서 저장
            // 나머지 등수도 마찬가지로 적용

            String sql = "update lotto set grade = ?, lottorankcount1 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount1);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount1 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }

            System.out.println("rankcount1 = " + rankcount1);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else if (count == 5)
        {
            rank = "2등";
            Lotto lotto = findById(findId);
            if(lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount2++;
                fixedrankcount2 = rankcount2;
                lotto.setLottorank2(fixedrankcount2);
            }
            else
            {
                rankcount2 = fixedrankcount2;
                lotto.setLottorank2(fixedrankcount2);
            }

            String sql = "update lotto set grade = ?, lottorankcount2 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount2);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount2 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }

            System.out.println("rankcount2 = " + rankcount2);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else if (count == 4)
        {
            rank = "3등";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount3++;
                fixedrankcount3 = rankcount3;
                lotto.setLottorank3(fixedrankcount3);
            }
            else
            {
                rankcount3 = fixedrankcount3;
                lotto.setLottorank3(fixedrankcount3);
            }

            String sql = "update lotto set grade = ?, lottorankcount3 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount3);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount3 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }


            System.out.println("rankcount3 = " + rankcount3);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else if (count == 3)
        {
            rank = "4등";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount4++;
                fixedrankcount4 = rankcount4;
                lotto.setLottorank4(fixedrankcount4);
            }
            else
            {
                rankcount4 = fixedrankcount4;
                lotto.setLottorank4(fixedrankcount4);
            }

            String sql = "update lotto set grade = ?, lottorankcount4 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount4);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount4 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }


            System.out.println("rankcount4 = " + rankcount4);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else if (count == 2)
        {
            rank = "5등";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount5++;
                fixedrankcount5 = rankcount5;
                lotto.setLottorank5(fixedrankcount5);
            }
            else
            {
                rankcount5 = fixedrankcount5;
                lotto.setLottorank5(fixedrankcount5);
            }

            String sql = "update lotto set grade = ?, lottorankcount5 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount5);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount5 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }


            System.out.println("rankcount5 = " + rankcount5);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else if (count == 1)
        {
            rank = "6등";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount6++;
                fixedrankcount6 = rankcount6;
                lotto.setLottorank6(fixedrankcount6);
            }
            else
            {
                rankcount6 = fixedrankcount6;
                lotto.setLottorank6(fixedrankcount6);
            }

            String sql = "update lotto set grade = ?, lottorankcount6 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount6);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount6 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }


            System.out.println("rankcount6 = " + rankcount6);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        else
        {
            rank = "꽝";
            Lotto lotto = findById(findId);
            if (lotto.getGrade() == null)
            {
                lotto.setGrade(rank);
                rankcount7++;
                fixedrankcount7 = rankcount7;
                lotto.setLottorank7(fixedrankcount7);
            }
            else
            {
                rankcount7 = fixedrankcount7;
                lotto.setLottorank7(fixedrankcount7);
            }
            String sql = "update lotto set grade = ?, lottorankcount7 = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, lotto.getGrade());
                preparedStatement.setInt(2, fixedrankcount7);
                preparedStatement.setLong(3, lotto.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("lottorankcount7 result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }
            System.out.println("rankcount7 = " + rankcount7);
            System.out.println();
            System.out.println(data.toString());
            System.out.println();
        }
        return rank;
    }

    @Override
    public Lotto findById(Long id) {
        return data.get(id);
    }

    @Override
    public int findMyRank(String myrank) {
        int checkcount;
        if (myrank.equals("1등"))
        {
            checkcount = rankcount1;
            System.out.println("1등카운트 = " + checkcount);
            return checkcount;
        }
        else if(myrank.equals("2등"))
        {
            checkcount = rankcount2;
            System.out.println("2등카운트 = " + checkcount);
            return checkcount;
        }
        else if (myrank.equals("3등"))
        {
            checkcount = rankcount3;
            System.out.println("3등카운트 = " + checkcount);
            return checkcount;
        }
        else if (myrank.equals("4등"))
        {
            checkcount = rankcount4;
            System.out.println("4등카운트 = " + checkcount);
            return checkcount;
        }
        else if (myrank.equals("5등"))
        {
            checkcount = rankcount5;
            System.out.println("5등카운트 = " + checkcount);
            return checkcount;
        }
        else if (myrank.equals("6등"))
        {
            checkcount = rankcount6;
            System.out.println("6등 카운트 = " + checkcount);
            return checkcount;
        }
        else
        {
            checkcount = rankcount7;
            System.out.println("꽝 카운트 = " + checkcount);
            return checkcount;
        }
    }

    @Override
    public void lottoBuyDateTime(Long id) {
        Lotto lotto = data.get(id);
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        // widthNano 함수로 시간 날짜에서 뒷자리 소수점을 제외
        lotto.setLottobuyDateTime(localDateTime);
        System.out.println("로또 구매 날짜 시간 = " + localDateTime);

        String sql = "update lotto set lottobuydatetime = ? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            String localbuydatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            preparedStatement.setString(1, localbuydatetime);
            preparedStatement.setLong(2, lotto.getId());


            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("lottobuydatetime result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public List<Lotto> findAll() {
        List<Lotto> lottoList = new ArrayList<>();
        String findAllSql = "select * from lotto";

        // db에 저장되어 있는 데이터 중 등수에 해당하는 데이터를 전부 가져옴

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllSql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {

            while (resultSet.next())
            {
                Lotto lotto = new Lotto();
                lotto.setLottorank1(resultSet.getInt("lottorankcount1"));
                lotto.setLottorank2(resultSet.getInt("lottorankcount2"));
                lotto.setLottorank3(resultSet.getInt("lottorankcount3"));
                lotto.setLottorank4(resultSet.getInt("lottorankcount4"));
                lotto.setLottorank5(resultSet.getInt("lottorankcount5"));
                lotto.setLottorank6(resultSet.getInt("lottorankcount6"));
                lotto.setLottorank7(resultSet.getInt("lottorankcount7"));

                lottoList.add(lotto);

                log.info("가져온 사용자 수: {}", lottoList.size());
                log.info("사용자: {}", lottoList);
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
        }

        return lottoList;
    }

    private Connection getConnection()
    {
        return DBConnectionUtility.getConnection();
    }
}
