package com.sliit.ssd.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sliit.ssd.client.config.SSLUtil;
import com.sliit.ssd.client.model.User;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;

@Controller
@SessionAttributes({"user","access_token"})

public class HomeController {
	
	
	@Value("${security.clientId}")
	private String clientId;
	
	@Value("${security.clientSecret}")
	private String clientSecret;
	
	@Value("${security.authorizationUri}")
	private String authorizationUri;
	
	@Value("${security.tokenUri}")
	private String tokenUri;
	
	@Value("${security.userInfoUri}")
	private String userInfoUri;
	
	@Value("${security.callbackUri}")
	private String callbackUri;
	
	RestTemplate restTemplate;


	public HomeController() {
		super();
		try {
			SSLUtil.turnOffSslChecking();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		restTemplate = new RestTemplate();
		
	}


	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	
	@RequestMapping("/")
	public String getIndex1(Model model, HttpSession session){
		
		
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		return "home";
	}
	
	@RequestMapping("/logoutUser")
	public String logsout(Model model){
		
			List<GrantedAuthority> ga=new ArrayList<GrantedAuthority>();
			ga.add(new SimpleGrantedAuthority("ROLE_USER"));
			SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key","Anonymous",ga));
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		return "home";
	}
	
	

	    @GetMapping(value = "/authorize")
  		public void authorize(HttpServletResponse res) throws JsonProcessingException, IOException {
  			
    	
  		   logger.info("Inside authorize");
  		 		 	
  		  
  			String url=authorizationUri;
  			url+="?response_type=code";
  		 	url+="&client_id="+clientId;
  		 	url+="&redirect_uri="+callbackUri;
  		 	url+="&scope=read";
		 
  			res.sendRedirect(url);
	    }
	    
	    
	    
	    @RequestMapping(value = "/callback")
	  		public String getToken(@RequestParam("code") String code,HttpServletRequest req,Model model,HttpSession session) throws JsonProcessingException, IOException {
	  			
	    	
	    		logger.info("Inside callback");
	    		logger.info("Code" + code);
	    		
	    	
	    		 
	  		 	ResponseEntity<String> response = null;

	  			  			
	  			String credentials = clientId+":"+clientSecret;
	  			String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

	  			HttpHeaders headers = new HttpHeaders();
	  			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	  			headers.add("Authorization", "Basic " + encodedCredentials);

	  			HttpEntity<String> request = new HttpEntity<String>(headers);

	  			String access_token_url = tokenUri;
	  			access_token_url += "?code=" + code;
	  			access_token_url += "&grant_type=authorization_code";
	  			access_token_url += "&redirect_uri="+callbackUri;
	  				  		

	  			response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

	  			
	  			
	  			ObjectMapper mapper = new ObjectMapper();
	  			JsonNode node = mapper.readTree(response.getBody());
	  			String token = node.path("access_token").asText();
	  			String tokenType = node.path("token_type").asText();
	    		logger.info("access_token callback" + token);

	  			session.setAttribute("access_token", token);

	  			  			
	  			
	  			// Use the access token for authentication
	  			HttpHeaders headers1 = new HttpHeaders();
	  			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
	  			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
	  			HttpEntity<String> entity = new HttpEntity<>(headers1);

	  			ResponseEntity<User> userResponse;
	  		   try {
	  			 userResponse = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, User.class);
	  		    } catch (HttpStatusCodeException e) {
	  		        // handle exception as needed or return ResponseEntity as below commented
		    		logger.info("HttpStatusCodeException" + e);

	  		         return e.getLocalizedMessage();

	  		    }
	    		logger.info("trwelkkkksks" );

	  			
	  			User loggedInUser =  userResponse.getBody();
	  			
	  			logger.info("User  ----- "+loggedInUser.getUserName());
	  			
	  			model.addAttribute("user", loggedInUser.getUserName());
	  			
	  			logger.info("User  ----- "+loggedInUser.getRoles());
	  			
	  			
	  			
	  			List<GrantedAuthority> ga=new ArrayList<GrantedAuthority>();
	  			loggedInUser.getRoles().getPermissions().forEach(permission->ga.add(new SimpleGrantedAuthority(permission.getPermissionName().toUpperCase())));
				ga.add(new SimpleGrantedAuthority("ROLE_"+loggedInUser.getRoles().getRoleName().toUpperCase()));
				
				model.addAttribute("role", ga.toString());
				
				session.setAttribute("role", token);
	  			
	  			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loggedInUser.getUserName(),loggedInUser.getPassword(),ga));

	  			return "homes";
	  		

	  			
	  		}
	    
	    
	    @RequestMapping(value = "/users")
  		public String getToken(Model model,HttpSession session,HttpServletResponse res) throws JsonProcessingException, IOException {
	    	
	    	
	    	
  		    logger.info("Inside utilities");
	    	
	    	// Use the access token for authentication
  			HttpHeaders headers1 = new HttpHeaders();
  			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
  			headers1.add("content-type",MediaType.APPLICATION_JSON_UTF8_VALUE);
  			HttpEntity<String> entity = new HttpEntity<>(headers1);

  			ResponseEntity<User> userResponse;
	  		   try {
	  			 userResponse =  restTemplate.exchange("http://localhost:8080/v1/users/", HttpMethod.GET, entity, User.class);
	  		    } catch (HttpStatusCodeException e) {
	  		        // handle exception as needed or return ResponseEntity as below commented

	  		         return e.getLocalizedMessage();

	  		    }
  				
  			model.addAttribute("users", userResponse.getBody());
  			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities() );
  		 			
	    	return "home";
  			
	    }

}