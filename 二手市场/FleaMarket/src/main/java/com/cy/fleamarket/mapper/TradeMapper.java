package com.cy.fleamarket.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.fleamarket.pojo.Trade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TradeMapper extends BaseMapper<Trade> {
    public List<Map<Object,Object>> selectSoldTradeByphone(String phone);

    public List<Map<Object,Object>> selectBuyTradeByphone(String phone);

    public Map<Object,Object> selectTradeCommodity(int id);
}
