package com.example.personalhealthassistantapp.data.interfaces

import com.example.personalhealthassistantapp.data.model.AlarmModel

interface AlarmScheduler {

    fun schedule(time: AlarmModel)
    fun cancel(time: AlarmModel)

}