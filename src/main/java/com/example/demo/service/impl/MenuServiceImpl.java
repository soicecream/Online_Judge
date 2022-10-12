package com.example.demo.service.impl;

import com.example.demo.entity.Menu;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-10-06
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus() {
        //         查询所有数据
        List<Menu> list = list();

        // 找到pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == 0).collect(Collectors.toList());

        for (Menu menu : parentNodes) {
            // 筛选所有的二级菜单  所有数据中pid=父级id的数据就是下级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));

            // 筛选所有的三级菜单
            for (Menu menu1 : menu.getChildren()) {
                menu1.setChildren(list.stream().filter(m -> menu1.getId().equals(m.getPid())).collect(Collectors.toList()));
            }
        }
        return parentNodes;
    }
}
