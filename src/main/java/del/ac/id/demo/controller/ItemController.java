package del.ac.id.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.mongodb.client.result.UpdateResult;

import del.ac.id.demo.jpa.Buy;
import del.ac.id.demo.jpa.Item;
import del.ac.id.demo.jpa.ItemRepository;
import del.ac.id.demo.jpa.Store;
import del.ac.id.demo.jpa.StoreRepository;


@RestController
public class ItemController {
	@Autowired 
	ItemRepository itemRepository;
	@Autowired MongoTemplate tampungData;
	@Autowired StoreRepository storeRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	
	@RequestMapping("/item")
	public ModelAndView item() {
		List<Item> items = itemRepository.findAll();
		ModelAndView mv = new ModelAndView("item");
		mv.addObject("items",items);
		
		return mv;
	}
	
	@RequestMapping("/store")
	public ModelAndView store() {
		List<Store> store = storeRepository.findAll();
		ModelAndView mv = new ModelAndView("store");
		mv.addObject("stores",store);
		return mv;
		}
	
	@GetMapping("/user/show/{id}")
	public ModelAndView show(@PathVariable (value="id") String id) {
		Optional<Item> item = itemRepository.findById(id);
		Query query = new Query(Criteria.where("id").is(id));
		List<Item> item2 = tampungData.find(query, Item.class);
		if(item2 != null) {
			Update update = new Update().inc("seen", 1);
			UpdateResult result = tampungData.updateFirst(query, update, Item.class);
		}
		
		item.get().setSeen(item.get().getSeen()+1);
		ModelAndView mv = new ModelAndView("show");
		mv.addObject("item", item);
		return mv;
		}
		
	
	
	@RequestMapping(value="/buyItem/{id}",method= RequestMethod.POST)
	public RedirectView insertData(@ModelAttribute Buy buy, BindingResult bindingResult,
			Model model,@PathVariable("id") String id,RedirectAttributes attributes) {
		if(bindingResult.hasErrors()) {
			System.out.println("Error");
		}
		Query query = new Query(Criteria.where("id").is(id));
		Item temp = mongoTemplate.findOne(query, Item.class);
		List<Item> items = itemRepository.findAll();
		ModelAndView mv = new ModelAndView("item");
		Item selectedItem;
		if(temp != null) {
			selectedItem = temp;
			model.addAttribute("item",selectedItem);
			double stock = selectedItem.getStock();
			double sisa = stock - buy.getTotal_item();
			double rating = selectedItem.getRating();
			mv.addObject("items",items);
			
			
			System.out.println("Total Item = "+buy.getTotal_item());
			System.out.println("Sisa Item = "+sisa);
			
			Update update = new Update().inc("seen",1);
			mongoTemplate.updateFirst(query,update, Item.class);
			
			if(sisa < 0) {
				attributes.addFlashAttribute("stockStatus", "Stock Tidak mencukupi, Hanya tersisa "+stock+" item");
				return new RedirectView("/startOrder/{id}");
			}
			else if(buy.getTotal_item() <= 0) {
				attributes.addFlashAttribute("stockStatus", "Pembelian Barang tidak boleh kurang atau sama dengan 0");
				return new RedirectView("/startOrder/{id}");
			}
			else if(buy.getTotal_item() > 0 && sisa >= 0) {
				double ratingNow = (rating + buy.getRating())/2;
				selectedItem.setStock(sisa);
				Update updateItem = new Update();
				updateItem.set("stock", sisa);
				updateItem.set("rating", ratingNow);
				updateItem.set("sold", buy.getTotal_item());
				mongoTemplate.updateFirst(query,updateItem, Item.class);
				return new RedirectView("/item");
			}
		}
		mv.addObject("items",items);
		return new RedirectView("/item");
	}
	@GetMapping(value="/startOrder/{id}")
	public ModelAndView startOrder(@ModelAttribute Item item, BindingResult bindingResult,
			Model model,@PathVariable("id") String id,RedirectAttributes attributes) {
		if(bindingResult.hasErrors()) {
			System.out.println("Error");
		}
		Buy buy = new Buy();
		ModelAndView mv = new ModelAndView("buyItem");
		Optional<Item> temp = itemRepository.findById(id);
		Item selectedItem;
		if(temp != null) {
			selectedItem = temp.get();
			model.addAttribute("item",selectedItem);
			System.out.println(item.getId());
			mv.addObject("item",selectedItem);
			mv.addObject("buy",buy);}
		return mv;
	}
	@GetMapping(value="/cancelBuy/{id}")
	public RedirectView cancelBuy(@ModelAttribute Item item, BindingResult bindingResult,
			Model model,@PathVariable("id") String id,RedirectAttributes attributes) {
		if(bindingResult.hasErrors()) {
			System.out.println("Error");
		}
		ModelAndView mv = new ModelAndView("item");
		List<Item> items = itemRepository.findAll();
		mv.addObject("items",items);
		return new RedirectView("/item");
	}
	
	@GetMapping("/item/show/admin/{id}")
	public ModelAndView showAdmin(@PathVariable (value="id") String id) {
		Optional<Item> item = itemRepository.findById(id);
		Query query = new Query(Criteria.where("id").is(id));
		List<Item> item2 = tampungData.find(query, Item.class);
		if(item2 != null) {
			Update update = new Update().inc("seen", 1);
			UpdateResult result = tampungData.updateFirst(query, update, Item.class);
		}
		item.get().setSeen(item.get().getSeen()+1);
		ModelAndView mv = new ModelAndView("showItem");
		mv.addObject("item", item);
		return mv;
	}
	
	@GetMapping("/item/showFormUpdate/admin/{id}")
	public ModelAndView showFormUpdate(@PathVariable(name="id") String id) {
		Optional<Item> item = itemRepository.findById(id);
		
		ModelAndView mv = new ModelAndView("updateItem");
		mv.addObject("item", item);
		return mv;
	}
	
	@GetMapping("/updateItem")
	public ModelAndView updateItem(@RequestParam(name="id") String id, @RequestParam(name="stock") double stock, @RequestParam(name="itemDetail.weight") double weight, @RequestParam(name="itemDetail.condition") String condition, @RequestParam(name="itemDetail.category") String category) {
		Optional<Item> item = itemRepository.findById(id);
		Query query = new Query(Criteria.where("id").is(id));
		List<Item> item2 = tampungData.find(query, Item.class);
		if(item2!=null) {
			Update update = new Update();
			update.set("stock", stock);
			update.set("item_detail.weight", weight);
			update.set("item_detail.condition", condition);
			update.set("item_detail.category", category);
			UpdateResult result = tampungData.updateFirst(query, update, Item.class);
		}
		ModelAndView mv = new ModelAndView("redirect:/admin");
		
		return mv;
	}
	
	 @GetMapping("/admin/show/store/item/{store_name}") 
	  public ModelAndView itemStore(@PathVariable(name="store_name") String store_name) {
			Query query = new Query(Criteria.where("store_name").is(store_name));
			List<Store> store = tampungData.find(query, Store.class);
			ModelAndView mv = new ModelAndView("store_item");
			mv.addObject("storeitems",store);
			return mv;
		}
}
