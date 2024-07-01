cd ~/Desktop

org1=$(docker ps | grep -i dev-peer0.org1 | grep -o 'Up[^*]*' | grep -o 'dev-peer0.org1[^*]*')
org2=$(docker ps | grep -i dev-peer0.org2 | grep -o 'Up[^*]*' | grep -o 'dev-peer0.org2[^*]*')

org1="${org1}:/root/chaincode-java/a.properties"
org2="${org2}:/root/chaincode-java/a.properties"

docker cp a.properties $org1
docker cp a.properties $org2
