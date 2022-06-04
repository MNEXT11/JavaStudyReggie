package rdm.example.reggie.dto;


import lombok.Data;
import rdm.example.reggie.entity.Setmeal;
import rdm.example.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
