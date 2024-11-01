package com.sv.edu.ufg.fis.amb.parcial3.routes

const val ROOT_MAIN_PAGE = "main"
const val ROOT_RECORD_PAGE = "record"
const val ROOT_VIDEOS_PAGE = "videos"
const val ROOT_PLAYER_PAGE = "player"
const val ROOT_PHOTO_PAGE = "photo"
const val ARG_PLAYER_PAGE = "uri"
const val ROOT_GALLERY_PAGE = "gallery"

sealed class Routes(
    val route: String
){
    object mainpage : Routes(route = ROOT_MAIN_PAGE)
    object recordpage : Routes(route = ROOT_RECORD_PAGE)
    object videospage : Routes(route = ROOT_VIDEOS_PAGE)
    object playerpage : Routes(route = "${ROOT_PLAYER_PAGE}?uri={${ARG_PLAYER_PAGE}}")
    object photopage : Routes(route = ROOT_PHOTO_PAGE)
    object gallerypage : Routes(route = ROOT_GALLERY_PAGE)
}