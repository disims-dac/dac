// SPDX-License-Identifier: MIT
pragma solidity ^0.8.15;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";

contract DisIMS is ERC721URIStorage, Ownable {

    uint256 counter = 0;
    constructor(address initialOwner) Ownable(initialOwner) ERC721("DISMis", "NFT") {
    }

    function mintNFT(address recipient, string memory tokenURI) public onlyOwner
            returns (uint256)
    {
        counter = counter + 1;
        uint256 newItemId = counter;
        _mint(recipient, newItemId);
        // tokenURL stores the SHA256 has of c or c'
        _setTokenURI(newItemId, tokenURI);
        return newItemId;
    }

    function getCounter() public view returns (uint256) {
        return counter;
    }
}