package com.sliit.ssd.client.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sliit.ssd.client.config.FileDownloadUtil;
import com.sliit.ssd.client.config.FileUploadUtil;
import com.sliit.ssd.client.config.SSLUtil;
import com.sliit.ssd.client.model.File;
import com.sliit.ssd.client.model.Message;
import com.sliit.ssd.client.model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
	
	@Value("${security.messagesUri}")
	private String messagesUri;
	
	
	@Value("${security.userUri}")
	private String userUri;
	
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
	public String getIndex1(Model model, HttpSession session,Principal principal){
		
		
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
	        return principal != null ? "redirect:/home" : "homeunauth";

	}
	
	@RequestMapping("/files")
	public String getFiles(Model model, HttpSession session,Principal principal){
		boolean hasGrants = false;
		for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities() ) {
    		logger.info("tttrtrtrtrwtfghbshdbjsad" + grantedAuthority);

			if(!grantedAuthority.getAuthority().equals("READ_MESSAGE")) {
				hasGrants = true;
			}
		}
		
		if (!hasGrants) {
    		return "unauthpage";
		}
		
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			File file =  new File();
  	        model.addAttribute("file", file);
	        return principal != null ? "files" : "homeunauth";

	}
	
	@RequestMapping("/home")
	public String getHome(Model model, HttpSession session,Principal principal){
		
			String retString = "homeNotSignedIn";
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			
			for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities() ) {
	    		logger.info("tttrtrtrtrwtfghbshdbjsad" + grantedAuthority);

				if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
		    		logger.info("tttrtrtrtrwtfghbshdbjsad" + grantedAuthority);
					model.addAttribute("admin",true);
				}
			}
    		logger.info("tttrtrtrtrwtfghbshdbjsad" + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

			if (principal != null) {
				HttpHeaders headers1 = new HttpHeaders();
	  			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
	  			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
	  			HttpEntity<String> entity = new HttpEntity<>(headers1);

	  			ResponseEntity<User[]> userResponse = null;;
	  		   try {
	  			 userResponse = restTemplate.exchange(userUri, HttpMethod.GET, entity, User[].class);
	  		    } catch (HttpStatusCodeException e) {
	  		        // handle exception as needed or return ResponseEntity as below commented
		    		logger.info("HttpStatusCodeException" + e);
	  		    }
	    		logger.info("trwelkkkksks" );

	  			if (userResponse != null) {
	  				User[] users =  userResponse.getBody();
		  	        model.addAttribute("allUsers", users);
	  			} else {
	  				retString = "redirect:/logoutUser";
	  			}
			} else {
  				retString = "redirect:/";
			}
			return retString;
		

	}
	

	@RequestMapping("/messages")
	public String getMessages(Model model, HttpSession session,Principal principal){
		
		
			String retString = "messages";

			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			if (principal != null) {
				HttpHeaders headers1 = new HttpHeaders();
	  			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
	  			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
	  			HttpEntity<String> entity = new HttpEntity<>(headers1);

	  			ResponseEntity<User[]> userResponse;
	  		   try {
	  			 userResponse = restTemplate.exchange(userUri, HttpMethod.GET, entity, User[].class);
	  		    } catch (HttpStatusCodeException e) {
	  		        // handle exception as needed or return ResponseEntity as below commented
		    		logger.info("HttpStatusCodeException" + e);

	  		         return e.getLocalizedMessage();

	  		    }
	    		logger.info("trwelkkkksks" );

	  			
	  			User[] users =  userResponse.getBody();
	  	        model.addAttribute("allUsers", users);

			}  else {
  				retString = "redirect:/";
			}
	        Message message = new Message();
	        model.addAttribute("message", message);
	        
		
	        if (principal != null) {
				HttpHeaders headers1 = new HttpHeaders();
	  			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
	  			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
	  			HttpEntity<String> entity = new HttpEntity<>(headers1);

	  			ResponseEntity<Message[]> messageResponse = null;;
	  		   try {
	  			 messageResponse = restTemplate.exchange(messagesUri + "/me" , HttpMethod.GET, entity, Message[].class);
	  		    } catch (HttpStatusCodeException e) {
	  		        // handle exception as needed or return ResponseEntity as below commented
		    		logger.info("HttpStatusCodeException" + e);
	  		    }
	    		logger.info("trwelkkkksks" );

	  			if (messageResponse != null) {
	  				Message[] messages =  messageResponse.getBody();
		  	        model.addAttribute("allMessages", messages);
	  			} else {
	  				retString = "redirect:/";
	  			}
			}
			return retString;

	}
	 
    @GetMapping("/addnew")
    public String addNewEmployee(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newemployee";
    }
    
	 
    @GetMapping("/addUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newuser";
    }
    
	@RequestMapping("/logoutUser")
	public String logsout(Model model){
		
			List<GrantedAuthority> ga=new ArrayList<GrantedAuthority>();
			ga.add(new SimpleGrantedAuthority("ROLE_USER"));
			SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key","Anonymous",ga));
			model.addAttribute("user",SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("role",SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
	        return "redirect:/";
	}
	
    @PostMapping("/registerUser")
    public String saveEmployee(@ModelAttribute("user") User user,HttpSession session) {
    		HttpHeaders headers1 = new HttpHeaders();
			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<User> entity = new HttpEntity<User>(user,headers1);
			ResponseEntity<User> userResponse;
    		logger.info("TrwelkHere" + session.getAttribute("access_token"));

		   try {
			 userResponse = restTemplate.exchange(userUri, HttpMethod.POST, entity, User.class);
		    } catch (HttpStatusCodeException e) {
		        // handle exception as needed or return ResponseEntity as below commented
    		logger.info("HttpStatusCodeException" + e);

		         return e.getLocalizedMessage();

		    }
        return "redirect:/";
    }
	
    @PostMapping("/sendMessage")
    public String saveMessage(@ModelAttribute("message") Message message,HttpSession session) {
		   logger.info("In Send message 2" );

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
		   logger.info("In Send message 2" );

			
			User loggedInUser =  userResponse.getBody();
			   logger.info("LOGGEDINUSER" + loggedInUser.getUserId());

    		message.setUserId(loggedInUser.getUserId());
    		headers1 = new HttpHeaders();
			headers1.add("Authorization", "Bearer "+ session.getAttribute("access_token"));
			headers1.add("content-type",MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<Message> entity1 = new HttpEntity<Message>(message,headers1);
			ResponseEntity<Message> messageResponse;
    		logger.info("TrwelkHere" + session.getAttribute("access_token"));

		   try {
			   messageResponse = restTemplate.exchange(messagesUri, HttpMethod.POST, entity1, Message.class);
		    } catch (HttpStatusCodeException e) {
		        // handle exception as needed or return ResponseEntity as below commented
    		logger.info("HttpStatusCodeException" + e);

		         return e.getLocalizedMessage();

		    }
        return "redirect:/messages";
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

	  	        return "redirect:/home";
	  		

	  			
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
	    
	    @PostMapping("/uploadFile")
	    public String saveUser( @RequestParam("file") MultipartFile multipartFile,HttpSession session,HttpServletResponse res) throws IOException {
  		    logger.info("Inside FileUpload");
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
  				   logger.info("In Send message 2" );

  					
  					User loggedInUser =  userResponse.getBody();
  					
  					if( loggedInUser.getRoleId() != 3) {
  				        return "unauthpage";

  					}
	        
  				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	      
	 
	        String uploadDir = "file-uploads/";
	        
	 
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	         
	        return "redirect:/files";
	    }
	    
	    @PostMapping("/downloadFile")
	    public void downloadFile(@PathVariable("fileName") String fileName,HttpServletResponse response) {
	        FileDownloadUtil downloadUtil = new FileDownloadUtil();
  		    logger.info("Inside download");
	        String uploadDir = "file-uploads/";
	    	String filepath = "e:\\"; 

			try {
				PrintWriter out = response.getWriter();
				FileInputStream fileInputStream = FileUploadUtil.downloadFile(uploadDir, fileName);
				response.setContentType("APPLICATION/OCTET-STREAM"); 
				  response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\""); 
				  
				  int i; 
				  while ((i=fileInputStream.read()) != -1) {
				    out.write(i); 
				  } 
				  fileInputStream.close(); 
				  out.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	    }
	    

}