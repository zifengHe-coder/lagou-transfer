import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IoCTest {
    @Test
    public void testIoC() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountDao accountDao =(AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);
    }

    @Test
    public void DITest() throws Exception {
        BeanFactory.init();
        TransferService transferService =(TransferService) BeanFactory.getBean("transferService");
        transferService.transfer("6029621011000", "6029621011001",100);
        System.out.println(transferService);
    }

}
