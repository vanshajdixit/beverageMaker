import entities.Ingredient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class BeverageMachineTest {
    
    private static final Logger log = LoggerFactory.getLogger(BeverageMachineTest.class);
    private static BeverageMachine beverageMachine;
    
    @Test
    public void testPrepareBeverage() {
        
        JSONParser parser = new JSONParser();
        List<Ingredient> stock = new ArrayList<>();
        try {
            
            /*
             *  change sample input below to test for other test cases.
             */
            
            String sampleInput = "{\n" + "  \"machine\": {\n" + "    \"outlets\": {\n" + "      \"count_n\": 3\n" + "    },\n" + "    \"total_items_quantity\": {\n" + "      \"hot_water\": 500,\n" + "      \"hot_milk\": 500,\n" + "      \"ginger_syrup\": 100,\n" + "      \"sugar_syrup\": 100,\n" + "      \"tea_leaves_syrup\": 100\n" + "    },\n" + "    \"beverages\": {\n" + "      \"hot_tea\": {\n" + "        \"hot_water\": 200,\n" + "        \"hot_milk\": 100,\n" + "        \"ginger_syrup\": 10,\n" + "        \"sugar_syrup\": 10,\n" + "        \"tea_leaves_syrup\": 30\n" + "      },\n" + "      \"hot_coffee\": {\n" + "        \"hot_water\": 100,\n" + "        \"ginger_syrup\": 30,\n" + "        \"hot_milk\": 400,\n" + "        \"sugar_syrup\": 50,\n" + "        \"tea_leaves_syrup\": 30\n" + "      },\n" + "      \"black_tea\": {\n" + "        \"hot_water\": 300,\n" + "        \"ginger_syrup\": 30,\n" + "        \"sugar_syrup\": 50,\n" + "        \"tea_leaves_syrup\": 30\n" + "      },\n" + "      \"green_tea\": {\n" + "        \"hot_water\": 100,\n" + "        \"ginger_syrup\": 30,\n" + "        \"sugar_syrup\": 50,\n" + "        \"green_mixture\": 30\n" + "      }\n" + "    }\n" + "  }\n" + "}";
            Object obj = parser.parse(sampleInput);
            JSONObject json = (JSONObject) obj;
            JSONObject machine = (JSONObject) json.get("machine");
            JSONObject outlets = (JSONObject) machine.get("outlets");
            Integer outletsCount = Integer.parseInt(outlets.get("count_n").toString());
            JSONObject ingredients = (JSONObject) machine.get("total_items_quantity");
            for(Object key : ingredients.keySet()) {
                Integer quantity = Integer.parseInt(ingredients.get(key).toString());
                stock.add(new Ingredient(key.toString(),quantity));
            }
            
            beverageMachine = new BeverageMachine(outletsCount, stock);
            List<Thread> threads = new ArrayList<>();
            JSONObject beverages = (JSONObject) machine.get("beverages");
            
            for(Object beverageKey : beverages.keySet()) {
                JSONObject beverageIngredients = (JSONObject) beverages.get(beverageKey.toString());
                Set<Ingredient> ingredientSet = new HashSet<>();
                for(Object nameKey : beverageIngredients.keySet()) {
                    Integer quantity = Integer.parseInt(beverageIngredients.get(nameKey).toString());
                    ingredientSet.add(new Ingredient(nameKey.toString(),quantity));
                }
                threads.add(prepareBeverage(beverageKey.toString(),ingredientSet));
            }
            
            for(Thread t : threads) {
                t.start();
            }
    
            try {
                for(Thread t : threads) { t.join(); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private Thread prepareBeverage(final String name, final Set<Ingredient> ingredients) {
        final Runnable runnable = () -> {
            try {
                beverageMachine.prepareBeverage(name,ingredients);
            } catch (final Exception ex) {
                System.out.println(ex.getMessage());
            }
        };
        
        return new Thread(runnable);
    }
}