package service;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public record GetGamesResult(ArrayList<GameData> games) {
}
