package com.zone.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;


import lombok.extern.slf4j.Slf4j;

/**
 * @Description: Controller基类
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 */
@Slf4j
public class BaseController<T, S extends IService<T>> {
    @Autowired
    S service;
}
