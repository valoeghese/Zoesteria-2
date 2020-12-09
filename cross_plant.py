import os
import json

class DataDir:
    def __init__(this, path):
        if not os.path.exists(path):
            os.makedirs(path)
        this.path = path

    def sub(this, subpath):
        return DataDir(this.path + "/" + subpath)

    def make(this, file, jsonobj):
        with open(this.path + "/" + file, "w+") as file:
            json.dump(jsonobj, file, indent=2)

MOD_ID = "zoesteria"

assets = DataDir("src/main/resources/assets/" + MOD_ID)
blockstates = assets.sub("blockstates")
blockmodels = assets.sub("models/block")
itemmodels = assets.sub("models/item")

loottables = DataDir("src/main/resources/data/" + MOD_ID + "/loot_tables/blocks")

block_id = input("block id: ")

modelloc = {}
textureloc = {}
itemTextureLoc = {}
childLoc = {"type": "minecraft:item", "conditions": [{"condition": "minecraft:match_tool","predicate": {"item": "minecraft:shears"}}]}

j_blockstate = {"variants": {"": modelloc}}
j_blockmodel = {"parent": "block/cross", "textures": textureloc}
j_itemmodel = {"parent": "item/generated", "textures": itemTextureLoc}
j_loottable = {"type": "minecraft:block","pools": [{"rolls": 1,"entries": [{"type": "minecraft:alternatives","children": [childLoc]}]}]}

while (block_id != ""):
    id_string = MOD_ID + ":block/" + block_id
    file_string = block_id + ".json"

    # Assets
    
    modelloc["model"] = id_string
    textureloc["cross"] = id_string
    itemTextureLoc["layer0"] = id_string

    blockstates.make(file_string, j_blockstate)
    blockmodels.make(file_string, j_blockmodel)
    itemmodels.make(file_string, j_itemmodel)

    # Data
    childLoc["name"] = MOD_ID + ":" + block_id
    loottables.make(file_string, j_loottable)
    
    block_id = input("block id: ")

