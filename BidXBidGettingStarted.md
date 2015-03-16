Getting Started With BidXBid Electronic Auctions Service


# Introduction #
This is a step by step guide for getting started with BidXBid Web services.
BidXBid provides Auctions web service designed to specifically implement a general trading arena between automated brokers.
Those brokers can trade digital and non digital goods and can be conducted on behalf of third parties or by a buying/selling software broker.


BidXBid provides the following capabilities:
  * Auctioning
  * Bidding
  * Auction and Bidding

examples of usage for the above capabilities:

Auctioning will be conducted by an entity the would like to sell some digital asset, lets say a picture taken in some certain event, with defined parameters [date, location etc.], with unknown price.

Bidding will be conducted by some entity that would like to purchase a digital asset (e.g. 100G for month on someones computer)

Auction and Bidding will be normally created by some service provider just like [Google AdWords](http://adwords.google.com) conducting an auction for each advertisement based on general pricing parameters of the costumers, buying advertisements in pages or search results containing words that are in their advertisement domain.

_Note that those examples refer to demand while the framework also supports supply auction as well_

# Create your first auction #
  1. Login to [BidXBid web site](http://www.BidXBid.com) with your Google account, you'll get free tier just enough to learn and test the framework.
  1. Check your email for your BidXBid Secret key
  1. Get ready for production is easy just register your PayPal account with BidXBid on your [account page](http://www.bidxbid.com/my/account)
  1. Checkout BidXBid client maven: svn checkout https://bidxbid.googlecode.com/svn/trunk/bidxbidclient

```
public class BidXBidExample 
{

	protected void remoteStandardAuction() {
		String email = "my@email.com";
		String secret = "dsafs786^&%^fggfgh";
		
		Globals.instance().setUserEmail(email);
		Globals.instance().setSecretKey(secret);
		
		//create the auction
		AuctionType auctionType = new AuctionType(AuctionPrimaryType.ENGLISH);
		auctionType.addSecondaryType(new SecondaryParam(AuctionSecondaryType.SUPPLY, 1d));
		auctionType.addPrimeryParam(PrimeryParamType.START_PRICE, 10d);
		auctionType.addPrimeryParam(PrimeryParamType.DELTA_PRICE, 0.001d);

		//add the items for sale 
		List<Item> items = new ArrayList<Item>();

		Auction auction = new Auction("EnglishTest", email,
				auctionType, items, "English auction test", new Date(),
				new DateTime().plusSeconds(200).toDate());

		//create auctioneer
		AuctioneerProxy auctioneerProxy = new AuctioneerProxy();
		Auctioneer auctioneer	= auctioneerProxy.createAndRegisterAuction(auction, secret);
		//run/conduct the auction and get the results
		auctioneer = auctioneerProxy.run(auctioneer);
		
		System.out.println(auction);
	}
}
```