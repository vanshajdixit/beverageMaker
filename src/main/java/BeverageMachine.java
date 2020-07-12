import entities.Ingredient;
import exceptions.IngredientsUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class BeverageMachine {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BeverageMachine.class);
    private AtomicInteger outlets;
    private Map<String,Integer> ingredientCapacity = new HashMap<String, Integer>();
    
    /**
     * contructor to initialize a beverage machine
     * @param outlets
     * @param ingredients
     */
    public BeverageMachine(final int outlets, final List<Ingredient> ingredients) {
        this.outlets = new AtomicInteger(outlets);
        for(Ingredient ingredient : ingredients) {
            if(ingredientCapacity.containsKey(ingredient.getName()))
                ingredientCapacity.put(ingredient.getName(),ingredientCapacity.get(ingredient.getName())+ingredient.getQuantity());
            else
                ingredientCapacity.put(ingredient.getName(),ingredient.getQuantity());
        }
    }
    
    public void prepareBeverage(final String name, final Set<Ingredient> ingredients) {
        while (outlets.get() == 0) {
            /*
             * Making thread wait until no outlet is free
             */
        }
        
        outlets.decrementAndGet();
        
        try {
            checkAvailableIngredients(ingredients);
            takeIngredients(ingredients);
            LOGGER.info("{} is prepared", name);
        } catch (IngredientsUnavailableException ex) {
            LOGGER.info("{} cannot be prepared because {}",name, ex.getMessage());
        }
        
        outlets.incrementAndGet();
    }
    
    /**
     * check if all ingredients are available and sufficient for a beverage
     * @param ingredients
     * @throws IngredientsUnavailableException
     */
    private void checkAvailableIngredients(final Set<Ingredient> ingredients) throws IngredientsUnavailableException {
        
        for(Ingredient ingredient : ingredients) {
            if (!ingredientCapacity.containsKey(ingredient.getName())){
                throw new IngredientsUnavailableException(ingredient.getName() + " is not available");
            }
            if (ingredientCapacity.get(ingredient.getName()) < ingredient.getQuantity()){
                throw new IngredientsUnavailableException("item " + ingredient.getName() + " is not sufficient");
            }
        }
    }
    
    /**
     * Take out ingredients for a drink if available, since method is synchronised,
     * it could be accessed only by a single thread at a time
     * @param ingredients
     * @throws IngredientsUnavailableException
     */
    private synchronized void takeIngredients(final Set<Ingredient> ingredients) throws IngredientsUnavailableException {
        checkAvailableIngredients(ingredients);
        for(Ingredient ingredient : ingredients) {
            ingredientCapacity.put(ingredient.getName(),ingredientCapacity.get(ingredient.getName())-ingredient.getQuantity());
        }
    }
    
}
