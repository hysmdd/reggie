package cn.imqinhao.dto;

import cn.imqinhao.entity.Setmeal;
import cn.imqinhao.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
