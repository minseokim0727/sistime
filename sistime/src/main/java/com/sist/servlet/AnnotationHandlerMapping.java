package com.sist.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {
	private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }
    
	@Override
	public void initialize() throws ServletException {
		try {
			initHandlerExecution(basePackages);
		} catch (ServletException e) {
			throw e;
		}
	}

	@Override
	public HandlerExecution getHandler(final HttpServletRequest req) {
        HandlerKey handlerKey = new HandlerKey(req);
        
        return handlerExecutions.get(handlerKey);
    }
	
	private void initHandlerExecution(final String[] basePackage) throws ServletException {
		final Set<Class<?>> controllers = getAllControllers(basePackage);
	        
		// 스트림의 flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
		//    : Stream에 있는 어떤 값에 mapper function이 적용될 때 결과가 다른 단일 값이 아닌 스트림(복수개의 값)을 리턴 
		//    : 각각의 값들이 모두 실행되면 여러개의 스트림이 생기며, 이를 하나로 합쳐서 결과로 반환
		//    : Array나 Object로 감싸져 있는 모든 원소를 단일 원소 스트림으로 반환
		// Stream의 collect(Collector<T, A, R> collector)
		//    : 필터링, 매핑된 요소들을 새로운 컬렉션에 수집해서 리턴
		List<Method> methods = controllers.stream()
			.flatMap(it -> Arrays.stream(it.getMethods()))
			.filter(it -> it.isAnnotationPresent(RequestMapping.class))
			.collect(Collectors.toList());
	        
		try {
			for (Method method : methods) {
				addHandlerExecutions(method);
			}
		} catch (ServletException e) {
			throw e;
		}
    }

    private void addHandlerExecutions(final Method method) throws ServletException {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping);
        HandlerExecution handlerExecution = new HandlerExecution(method);

        for (HandlerKey handlerKey : handlerKeys) {
        	// 동일한 uri가 존재하는 경우 예외를 던짐
        	if(handlerExecutions.containsKey(handlerKey)) {
        		throw new ServletException(handlerKey.toString() + " : URI 가 중복되었습니다.");
        	}
        	
        	handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
    
    private List<HandlerKey> getHandlerKeys(final RequestMapping requestMapping) {
        if (requestMapping != null) {
        	String url = requestMapping.value();
            RequestMethod []requestMethods = requestMapping.method();
            if(requestMethods.length == 0) {
            	requestMethods = new RequestMethod[] {RequestMethod.GET, RequestMethod.POST};
            }
            
            // Stream의 map(Function<? super T,? extends R> mapper)
            //    : Stream에 있는 값들을 하나 하나 다른 값으로 매핑한 후, 결과를 스트림으로 리턴
            return Arrays.stream(requestMethods)
                    .map(method -> new HandlerKey(url, method))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }	    
  
    // Controller Scan 시작 ---------------------------------
    private Set<Class<?>> getAllControllers(final String[] basePackages) {
    	Set<Class<?>> set = new LinkedHashSet<Class<?>>();
    	
    	for(String basePackage : basePackages) {
    		set.addAll(scan(basePackage));
    	}
    	
        return set;
    }

    private Set<Class<?>> scan(String basePackage) {
    	// 현재 실행 중인 쓰레드의 클래스 로더 반환
       	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
       	String path = basePackage.replace('.', '/');

       	Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

       	try {
       		List<File> files = new ArrayList<File>();
    		Enumeration<URL> resources = classLoader.getResources(path);
    		while (resources.hasMoreElements()) {
    			URL resource = resources.nextElement();
    			files.add(new File(resource.getFile()));
    		}
    		for (File file : files) {
    			if (file.isDirectory()) {
    				classes.addAll(findClasses(file, basePackage));
    			}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

       	return classes;
    }
    
    private Set<Class<?>> findClasses(File directory, String basePackage) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		if (! directory.exists()) {
			return classes;
		}
		
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file, basePackage + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				String className = basePackage + '.' + file.getName().substring(0, file.getName().length() - 6);
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				try {
					// forName(String className, boolean initialize, ClassLoader classLoader) 
					//   initialize : 클래스 초기화 여부(기본:true-static 초기화블럭이실행됨)
					//   classLoader : 현재의 클래스를 정의하는 클래스 로더
					Class<?> cls = Class.forName(className, false, classLoader);
					if(cls.isAnnotationPresent(Controller.class)) {
						classes.add(Class.forName(className, false, classLoader));
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		return classes;
    }	    
	// Controller Scan 종료 ---------------------------------
}
