package com.example.demo.service.impl;

import com.example.demo.entity.Problem;
import com.example.demo.mapper.ProblemMapper;
import com.example.demo.service.IProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-10-14
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

}
