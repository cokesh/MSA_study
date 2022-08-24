package egovframework.msa.sample.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import egovframework.msa.sample.service.CustomerApiService;

@Service
public class CustomerApiServiceImpl implements CustomerApiService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	@HystrixCommand(fallbackMethod = "getCustomerDetailFallback"
//			,commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="500") }
//			,commandProperties = {@HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="1"),
//							      @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="50")}
			      
			) //exception을 가로채서 원하는 메서드를 호출시킨다.
	public String getCustomerDetail(String customerId) {
		return restTemplate.getForObject("http://localhost:8082/customers/" + customerId, String.class);
	}
	// Throwable 타입을 매개변수로 받아서 모든 예외를 처리할 수 있게끔 설정함₩
	public String getCustomerDetailFallback(String customerId, Throwable ex) {
		System.out.println("Error:" + ex.getMessage());
		return "고객정보 조회가 지연되고 있습니다.";
	}
}
