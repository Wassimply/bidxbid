package com.bidxbid.client.examples;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.bidxbid.api.configuration.Globals;
import com.bidxbid.api.model.auction.Auction;
import com.bidxbid.api.model.auction.AuctionType;
import com.bidxbid.api.model.auction.SecondaryParam;
import com.bidxbid.api.model.auction.process.ShuffleBiddersListProcess;
import com.bidxbid.api.model.auction.process.StandardApproveProcess;
import com.bidxbid.api.model.auction.process.StandardAskProcess;
import com.bidxbid.api.model.auction.process.StandardAuctionClose;
import com.bidxbid.api.model.auctioneer.Auctioneer;
import com.bidxbid.api.model.auctioneer.Auctioneer.PROCESS;
import com.bidxbid.api.model.bidder.Bidder;
import com.bidxbid.api.model.bidder.policy.BidderPolicy;
import com.bidxbid.api.model.bidder.policy.BudgetBidderPolicy;
import com.bidxbid.api.model.items.Item;
import com.bidxbid.api.model.user.User;
import com.bidxbid.api.rest.client.AuctionProxy;
import com.bidxbid.api.rest.client.AuctioneerProxy;
import com.bidxbid.api.rest.client.UserProxy;
import com.bidxbid.api.type.AuctionPrimaryType;
import com.bidxbid.api.type.AuctionSecondaryType;
import com.bidxbid.api.type.PrimeryParamType;

/**
 * BidXBid Hello World Example!
 *
 */
public class MyAuctioneer 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Auctioneer!" );
    }

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

	protected void localEnglishAuction() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item("test", "seller@example.com"));

		AuctionType auctionType = new AuctionType(AuctionPrimaryType.ENGLISH);
		auctionType.addSecondaryType(new SecondaryParam(
				AuctionSecondaryType.SUPPLY, 1d));
		auctionType.addPrimeryParam(PrimeryParamType.START_PRICE, 10d);
		auctionType.addPrimeryParam(PrimeryParamType.DELTA_PRICE, 0.001d);

		Auction auction = new Auction("EnglishTest", "seller@example.com",
				auctionType, items, "English auction test", new Date(),
				new DateTime().plusSeconds(200).toDate());
		List<Bidder> bidders = new ArrayList<Bidder>();
		List<BidderPolicy> policies = new ArrayList<BidderPolicy>();
		policies.add(new BudgetBidderPolicy(0));
		for (int i = 0; i < 10; i++) {
			bidders.add(new Bidder("bidder_" + i, "bidder_" + i
					+ "@example.com", i + 1f, policies));
		}

		Auctioneer  english = new Auctioneer(auction, 1l, bidders);

		english.addProcess(new StandardApproveProcess(), PROCESS.INIT);
		english.addProcess(new ShuffleBiddersListProcess(), PROCESS.PREASK);
		english.addProcess(new StandardAskProcess(), PROCESS.ASK);
		english.addProcess(new StandardAuctionClose(), PROCESS.FINALIZE);

		english.run();
		System.out.println(english.getResult());
	}    
    
}
