package de.htwg.sudoku.model;

/**
 * Patterns: Abstract Factory This is the abstract factory for the strategy to
 * create grids
 */
public abstract class GridCreateStrategyFactory {
/* Fields */
    private static GridCreateStrategyTemplate strategy = new RemovePairsGridCreateStrategy();

/* Constructors */
    private GridCreateStrategyFactory(){

    }

/* Getter and Setter */
    public static void setStrategy(GridCreateStrategyTemplate strat) {
        strategy = strat;
    }

    public static GridCreateStrategyTemplate getInstance() {
        return strategy;
    }

    public static GridCreateStrategyTemplate getStrategy() {
        return strategy;
    }

}
