package com.reinlin.data.model.local

import com.reinlin.domain.model.Data

internal val Data.Exhibit.toEntity: DbZoo.Exhibit
    get() = DbZoo.Exhibit(this.id, this)

internal val DbZoo.Exhibit.toData: Data.Exhibit
    get() = exhibit.run { Data.Exhibit(id, name, info, category, picUrl, URL) }

internal val Data.Plant.toEntity: DbZoo.Plant
    get() = DbZoo.Plant(this.id, this)

internal val DbZoo.Plant.toData: Data.Plant
    get() = plant.run { Data.Plant(id, name, nameEn, nameLatin, alsoKnown, locations, familyType, genusType, briefInfo, detail, function, picUrl) }

