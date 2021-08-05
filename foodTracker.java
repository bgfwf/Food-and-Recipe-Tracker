// Inventory tracker

import java.io.*;
import java.util.*;


class Recipe {
	String title;
	HashMap<String,Integer> ingredients;
	Recipe(String s) {
		this.title = s;
		ingredients = new HashMap<String,Integer>();
	}
}


public class foodTracker {
	public static void main(String[] args) throws Exception // args[0] is inventory.txt
	{
		// Add foods to HashMap
		HashMap<String,Integer> inventory = new HashMap<String,Integer>();
		File file = new File(args[0]);
		BufferedReader infile = new BufferedReader(new FileReader(file));
		while(infile.ready())
		{
			String currentFood = infile.readLine();
			if (inventory.keySet().contains(currentFood))
				inventory.put(currentFood,inventory.get(currentFood)+1);
			else
				inventory.put(currentFood,1);
		}
		
		HashMap<String,Recipe> recipes = new HashMap<String,Recipe>();
		File file2 = new File(args[1]);
		BufferedReader infile2 = new BufferedReader(new FileReader(file2));
		while(infile2.ready())
		{
			String line = infile2.readLine();
			String[] tokens = line.split("\\s+");
			String recipeName = tokens[0];
			Recipe recipe = new Recipe(recipeName);
			for (int i = 1; i < tokens.length; i++)
			{
				int quantity = Integer.parseInt(tokens[i++]);
				String ingredient = tokens[i];
				recipe.ingredients.put(ingredient,quantity);
			}
			recipes.put(recipeName,recipe);
		}
		
		// initialize recipeBase
		// how to save entered food and recipes between programs? Store "inventory" in file after program,
		// load "inventory" from file in the beginning
		
		
		// Terminal gui options: (1) add food by quantity (2) remove food by quantity
		// (3) add recipe, then enter ingredients one at a time (4) check if we have enough food for recipe (5) check how much of a certain food is available
		Scanner input = new Scanner(System.in);
		while (true)
		{
			System.out.println("i. add food by quantity");
			System.out.println("ii. remove food by quantity");
			System.out.println("iii. see how much of a food is available");
			System.out.println("iv. add recipe");
			System.out.println("v. check recipe ingredients");
			System.out.println("vi. see if inventory has ingredients for recipe");
			System.out.println("vii. see recipes that are available with current food");
			System.out.println("type '!' to EXIT");
			String option = input.next();
		
			if (option.equals("i"))
			{
				System.out.println("Enter the food added: ");
				String food = input.next();
				System.out.println("Enter the quantity added: ");
				int quantity = input.nextInt();
				if (inventory.keySet().contains(food))
					inventory.put(food,inventory.get(food)+quantity);
				else
					inventory.put(food,quantity);
			}
			if (option.equals("ii"))
			{
				System.out.println("Enter the food removed: ");
				String food = input.next();
				System.out.println("Enter the quantity removed: ");
				int quantity = input.nextInt();
				if (inventory.keySet().contains(food))
					inventory.put(food,inventory.get(food)-quantity);
			}
			if (option.equals("iii"))
			{
				System.out.println("Check inventory of which food: ");
				String food = input.next();
				if (inventory.keySet().contains(food))
					System.out.println("The quantity remaining of " + food + " is: " + inventory.get(food));
				else
					System.out.println("Food is unavailable");
			}
			if (option.equals("iv"))
			{
				System.out.print("Enter the name of the recipe: ");
				String recipeName = input.next();
				Recipe recipe = new Recipe(recipeName);
				System.out.println("Enter ingredients one at a time. Enter 'done' when finished");
				while(true) {
					System.out.print("Enter an ingredient: ");
					String ingredient = input.next();
					if (ingredient.equals("done"))
						break;
					System.out.print("Enter a quantity: ");
					int quantity = input.nextInt();
					recipe.ingredients.put(ingredient,quantity);
				}
				recipes.put(recipe.title,recipe);
			}
			if (option.equals("v"))
			{
				System.out.println("Check ingredients for which recipe: ");
				String recipe = input.next();
				if (recipes.keySet().contains(recipe))
				{
					Recipe currentRecipe = recipes.get(recipe);
					for (String ingredient : currentRecipe.ingredients.keySet())
					{
						System.out.print(currentRecipe.ingredients.get(ingredient) + " " + ingredient + ", ");
					}
				}
				else
					System.out.println("recipe not in database");
			}
			if (option.equals("vi")) // *****HERE*****
			{
				boolean canMake = true;
				System.out.print("Which recipe: ");
				String recipeName = input.next();
				if (!recipes.keySet().contains(recipeName))
				{
					System.out.print("Recipe not found");
					break; // does this work like this???
				}
				Recipe recipe = recipes.get(recipeName);
				for (String ingredient : recipe.ingredients.keySet())
				{
					if (!inventory.keySet().contains(ingredient) || inventory.get(ingredient) < recipe.ingredients.get(ingredient))
					{
						canMake = false;
					}
				}
				if (canMake)
					System.out.print("All ingredients are present in inventory");
				else
					System.out.print("Missing ingredients"); // Which ingredients are missing
			}
			if (option.equals("vii"))
			{
				ArrayList<String> recipesCanMake = new ArrayList<String>();
				for (String s : recipes.keySet())
				{
					boolean canMake = true;
					Recipe currentRecipe = recipes.get(s);
					for (String ingredient : currentRecipe.ingredients.keySet())
					{
						if (!inventory.keySet().contains(ingredient) || inventory.get(ingredient) < currentRecipe.ingredients.get(ingredient))
						{
							canMake = false;
						}
					}
					if (canMake == true)
						recipesCanMake.add(currentRecipe.title);
				}
				if (recipesCanMake.size() == 0)
					System.out.println("No recipes can be made");
				else
				{
					System.out.println("Recipes available with current ingredients: ");
					for (String s : recipesCanMake)
					{
						System.out.println(s);
					}
				}
			}
			if (option.equals("!"))
				break;
			System.out.println();
			System.out.println();
		}
		
		// Rewrite HashMap to inventory.txt
		File writeFile = new File("inventory.txt");
		FileWriter fw = new FileWriter(writeFile); // To erase file?
		FileOutputStream fos = new FileOutputStream(writeFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		for (String s : inventory.keySet())
		{
			System.out.println("Adding: " + s);
			while (inventory.get(s) != 0)
			{
				bw.write(s);
				bw.newLine();
				inventory.put(s,inventory.get(s)-1);
			}
		}
		bw.close();
		
		File writeFile2 = new File("recipes.txt");
		FileWriter fw2 = new FileWriter(writeFile2); // To erase file?
		FileOutputStream fos2 = new FileOutputStream(writeFile2);
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
		for (String s : recipes.keySet())
		{
			String totalRecipe = s;
			Recipe currentRecipe = recipes.get(s);
			System.out.println("Adding: " + s);
			for (String ingredient : currentRecipe.ingredients.keySet())
			{
				totalRecipe += " ";
				totalRecipe += currentRecipe.ingredients.get(ingredient);
				totalRecipe += " ";
				totalRecipe += ingredient;
			}
			bw2.write(totalRecipe);
			bw2.newLine();
		}
		bw2.close();
	}
}
