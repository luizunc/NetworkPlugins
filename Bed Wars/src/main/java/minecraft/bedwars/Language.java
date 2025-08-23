package minecraft.bedwars;

import minecraft.core.core.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Language {
  
  public static final String LOGGER_PREFIX = "[LANGUAGE]";
  
  // Configurações de moedas e recompensas
  public static int options$coins$wins = 50;
  public static int options$coins$beds = 25;
  public static int options$coins$kills = 5;
  public static int options$start$waiting = 45;
  public static int options$start$full = 10;
  
  // Eventos do jogo
  public static List<String> options$events$all$timings = Arrays
      .asList("DIAMOND:280", "EMERALD:480", "DIAMOND:680", "EMERALD:880", "BEDDESTROY:1240", "FIM:16840");
  public static int options$regen$block_regen$per_tick = 20000;
  public static boolean options$regen$world_reload = true;
  
  public static String options$events$diamond = "Diamante {tier}";
  public static String options$events$emerald = "Esmeralda {tier}";
  public static String options$events$beddestroy = "Destruição de Camas";
  public static String options$events$end = "Fim de Jogo";
  
  // Geradores
  public static int options$generator$diamond$countdown_tier_1 = 30;
  public static int options$generator$diamond$countdown_tier_2 = 23;
  public static int options$generator$diamond$countdown_tier_3 = 15;
  public static int options$generator$emerald$countdown_tier_1 = 65;
  public static int options$generator$emerald$countdown_tier_2 = 50;
  public static int options$generator$emerald$countdown_tier_3 = 35;
  public static double options$team_generator$emerald$countdown = 7.0;
  public static double options$team_generator$gold$countdown = 3.0;
  public static double options$team_generator$iron$countdown = 1.0;
  
  // Scoreboards
  public static long scoreboards$scroller$every_tick = 1;
  public static List<String> scoreboards$scroller$titles = Arrays
      .asList("§a§lBED WARS", "§6§lB§a§lED WARS", "§f§lB§6§lE§a§lD WARS", "§f§lBE§6§lD §a§lWARS",
          "§f§lBED §6§lW§a§lARS", "§f§lBED W§6§lA§a§lRS", "§f§lBED WA§6§lR§a§lS",
          "§f§lBED WAR§6§lS", "§f§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§f§lBED WARS", "§f§lBED WARS",
          "§f§lBED WARS", "§f§lBED WARS", "§f§lBED WARS", "§f§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§f§lBED WARS", "§f§lBED WARS", "§f§lBED WARS", "§f§lBED WARS", "§f§lBED WARS",
          "§f§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS",
          "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS", "§a§lBED WARS");
  public static String scoreboards$time$waiting = "Aguardando...";
  public static String scoreboards$time$starting = "Iniciando em §a{time}s";
  public static List<String> scoreboards$lobby = Arrays
      .asList("", " §eGeral", "  Vitorias: §a%Core_BedWars_wins%", "  Abates Finais: §a%Core_BedWars_finalkills%",
          "  Abates Gerais: §a%Core_BedWars_kills%", "", "  Camas Destr.: §a%Core_BedWars_beds%", "  Partidas: §a%Core_BedWars_games%", "",
          " Coins: §6%Core_BedWars_coins%", " Cash: §b%Core_cash%", "", " §7www.redeslick.com", " ");
  public static List<String> scoreboards$waiting =
      Arrays.asList("", "  Mapa: §a{map}", "  Jogadores: §a{players}/{max_players}",
          "", "  {time}", "", "  §7www.redeslick.com", "");
  public static List<String> scoreboards$ingame = Arrays
      .asList("", " Próximo Evento:", " §a{next_event}", "", " {red}", " {blue}", " {green}", " {yellow}",
          " {cyan}", " {white}", " {pink}", " {gray}", "", " §7www.redeslick.com", " ");
  
  // Chat
  public static String chat$delay = "§cAguarde mais {time}s para falar novamente.";
  public static String chat$color$default = "§7";
  public static String chat$color$custom = "§f";
  public static String chat$format$ingame$team = "{team} {player}{color}: {message}";
  public static String chat$format$ingame$global = "§6[GLOBAL] {team} {player}{color}: {message}";
  public static String chat$format$lobby = "{player}{color}: {message}";
  public static String chat$format$spectator = "§8[Espectador] {player}{color}: {message}";
  
  // Lobby
  public static String lobby$achievement = " \n§aVocê completou o desafio §f{name}\n ";
  public static String lobby$broadcast = "{player} " + "%Core_entrymessage%";
  public static boolean lobby$tab$enabled = true;
  public static String lobby$tab$header = " \n§b§lREDE SLICK\n  §fredeslick.com\n ";
  public static String lobby$tab$footer =
      " \n \n§aForúm: §fredeslick.com/forum\n§aTwitter: §f@RedeSlick\n§aDiscord: §fredeslick.com/discord\n \n                                          §bAdquira VIP acessando: §floja.redeslick.com                                          \n ";
  
  public static long lobby$leaderboard$minutes = 30;
  public static String lobby$leaderboard$empty = "§7...";
  
  public static List<String> lobby$leaderboard$wins$hologram = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8({page_info})");
  
  // Páginas separadas para vitórias
  public static List<String> lobby$leaderboard$wins$page1 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(1/10)");
  
  public static List<String> lobby$leaderboard$wins$page2 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e20. {name_20} §7- §e{stats_20}", "§e19. {name_19} §7- §e{stats_19}",
          "§e18. {name_18} §7- §e{stats_18}", "§e17. {name_17} §7- §e{stats_17}",
          "§e16. {name_16} §7- §e{stats_16}", "§e15. {name_15} §7- §e{stats_15}",
          "§e14. {name_14} §7- §e{stats_14}", "§e13. {name_13} §7- §e{stats_13}",
          "§e12. {name_12} §7- §e{stats_12}", "§e11. {name_11} §7- §e{stats_11}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(2/10)");
  
  public static List<String> lobby$leaderboard$wins$page3 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e30. {name_30} §7- §e{stats_30}", "§e29. {name_29} §7- §e{stats_29}",
          "§e28. {name_28} §7- §e{stats_28}", "§e27. {name_27} §7- §e{stats_27}",
          "§e26. {name_26} §7- §e{stats_26}", "§e25. {name_25} §7- §e{stats_25}",
          "§e24. {name_24} §7- §e{stats_24}", "§e23. {name_23} §7- §e{stats_23}",
          "§e22. {name_22} §7- §e{stats_22}", "§e21. {name_21} §7- §e{stats_21}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(3/10)");
  
  public static List<String> lobby$leaderboard$wins$page4 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e40. {name_40} §7- §e{stats_40}", "§e39. {name_39} §7- §e{stats_39}",
          "§e38. {name_38} §7- §e{stats_38}", "§e37. {name_37} §7- §e{stats_37}",
          "§e36. {name_36} §7- §e{stats_36}", "§e35. {name_35} §7- §e{stats_35}",
          "§e34. {name_34} §7- §e{stats_34}", "§e33. {name_33} §7- §e{stats_33}",
          "§e32. {name_32} §7- §e{stats_32}", "§e31. {name_31} §7- §e{stats_31}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(4/10)");
  
  public static List<String> lobby$leaderboard$wins$page5 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e50. {name_50} §7- §e{stats_50}", "§e49. {name_49} §7- §e{stats_49}",
          "§e48. {name_48} §7- §e{stats_48}", "§e47. {name_47} §7- §e{stats_47}",
          "§e46. {name_46} §7- §e{stats_46}", "§e45. {name_45} §7- §e{stats_45}",
          "§e44. {name_44} §7- §e{stats_44}", "§e43. {name_43} §7- §e{stats_43}",
          "§e42. {name_42} §7- §e{stats_42}", "§e41. {name_41} §7- §e{stats_41}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(5/10)");
  
  public static List<String> lobby$leaderboard$wins$page6 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e60. {name_60} §7- §e{stats_60}", "§e59. {name_59} §7- §e{stats_59}",
          "§e58. {name_58} §7- §e{stats_58}", "§e57. {name_57} §7- §e{stats_57}",
          "§e56. {name_56} §7- §e{stats_56}", "§e55. {name_55} §7- §e{stats_55}",
          "§e54. {name_54} §7- §e{stats_54}", "§e53. {name_53} §7- §e{stats_53}",
          "§e52. {name_52} §7- §e{stats_52}", "§e51. {name_51} §7- §e{stats_51}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(6/10)");
  
  public static List<String> lobby$leaderboard$wins$page7 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e70. {name_70} §7- §e{stats_70}", "§e69. {name_69} §7- §e{stats_69}",
          "§e68. {name_68} §7- §e{stats_68}", "§e67. {name_67} §7- §e{stats_67}",
          "§e66. {name_66} §7- §e{stats_66}", "§e65. {name_65} §7- §e{stats_65}",
          "§e64. {name_64} §7- §e{stats_64}", "§e63. {name_63} §7- §e{stats_63}",
          "§e62. {name_62} §7- §e{stats_62}", "§e61. {name_61} §7- §e{stats_61}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(7/10)");
  
  public static List<String> lobby$leaderboard$wins$page8 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e80. {name_80} §7- §e{stats_80}", "§e79. {name_79} §7- §e{stats_79}",
          "§e78. {name_78} §7- §e{stats_78}", "§e77. {name_77} §7- §e{stats_77}",
          "§e76. {name_76} §7- §e{stats_76}", "§e75. {name_75} §7- §e{stats_75}",
          "§e74. {name_74} §7- §e{stats_74}", "§e73. {name_73} §7- §e{stats_73}",
          "§e72. {name_72} §7- §e{stats_72}", "§e71. {name_71} §7- §e{stats_71}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(8/10)");
  
  public static List<String> lobby$leaderboard$wins$page9 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e90. {name_90} §7- §e{stats_90}", "§e89. {name_89} §7- §e{stats_89}",
          "§e88. {name_88} §7- §e{stats_88}", "§e87. {name_87} §7- §e{stats_87}",
          "§e86. {name_86} §7- §e{stats_86}", "§e85. {name_85} §7- §e{stats_85}",
          "§e84. {name_84} §7- §e{stats_84}", "§e83. {name_83} §7- §e{stats_83}",
          "§e82. {name_82} §7- §e{stats_82}", "§e81. {name_81} §7- §e{stats_81}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(9/10)");
  
  public static List<String> lobby$leaderboard$wins$page10 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e100. {name_100} §7- §e{stats_100}", "§e99. {name_99} §7- §e{stats_99}",
          "§e98. {name_98} §7- §e{stats_98}", "§e97. {name_97} §7- §e{stats_97}",
          "§e96. {name_96} §7- §e{stats_96}", "§e95. {name_95} §7- §e{stats_95}",
          "§e94. {name_94} §7- §e{stats_94}", "§e93. {name_93} §7- §e{stats_93}",
          "§e92. {name_92} §7- §e{stats_92}", "§e91. {name_91} §7- §e{stats_91}", "",
          "§e§lVITÓRIAS", "§b§lTOP 100 §8(10/10)");
  public static List<String> lobby$leaderboard$kills$hologram = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8({page_info})");
  
  // Páginas separadas para abates
  public static List<String> lobby$leaderboard$kills$page1 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(1/10)");
  
  public static List<String> lobby$leaderboard$kills$page2 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e20. {name_20} §7- §e{stats_20}", "§e19. {name_19} §7- §e{stats_19}",
          "§e18. {name_18} §7- §e{stats_18}", "§e17. {name_17} §7- §e{stats_17}",
          "§e16. {name_16} §7- §e{stats_16}", "§e15. {name_15} §7- §e{stats_15}",
          "§e14. {name_14} §7- §e{stats_14}", "§e13. {name_13} §7- §e{stats_13}",
          "§e12. {name_12} §7- §e{stats_12}", "§e11. {name_11} §7- §e{stats_11}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(2/10)");
  
  public static List<String> lobby$leaderboard$kills$page3 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e30. {name_30} §7- §e{stats_30}", "§e29. {name_29} §7- §e{stats_29}",
          "§e28. {name_28} §7- §e{stats_28}", "§e27. {name_27} §7- §e{stats_27}",
          "§e26. {name_26} §7- §e{stats_26}", "§e25. {name_25} §7- §e{stats_25}",
          "§e24. {name_24} §7- §e{stats_24}", "§e23. {name_23} §7- §e{stats_23}",
          "§e22. {name_22} §7- §e{stats_22}", "§e21. {name_21} §7- §e{stats_21}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(3/10)");
  
  public static List<String> lobby$leaderboard$kills$page4 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e40. {name_40} §7- §e{stats_40}", "§e39. {name_39} §7- §e{stats_39}",
          "§e38. {name_38} §7- §e{stats_38}", "§e37. {name_37} §7- §e{stats_37}",
          "§e36. {name_36} §7- §e{stats_36}", "§e35. {name_35} §7- §e{stats_35}",
          "§e34. {name_34} §7- §e{stats_34}", "§e33. {name_33} §7- §e{stats_33}",
          "§e32. {name_32} §7- §e{stats_32}", "§e31. {name_31} §7- §e{stats_31}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(4/10)");
  
  public static List<String> lobby$leaderboard$kills$page5 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e50. {name_50} §7- §e{stats_50}", "§e49. {name_49} §7- §e{stats_49}",
          "§e48. {name_48} §7- §e{stats_48}", "§e47. {name_47} §7- §e{stats_47}",
          "§e46. {name_46} §7- §e{stats_46}", "§e45. {name_45} §7- §e{stats_45}",
          "§e44. {name_44} §7- §e{stats_44}", "§e43. {name_43} §7- §e{stats_43}",
          "§e42. {name_42} §7- §e{stats_42}", "§e41. {name_41} §7- §e{stats_41}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(5/10)");
  
  public static List<String> lobby$leaderboard$kills$page6 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e60. {name_60} §7- §e{stats_60}", "§e59. {name_59} §7- §e{stats_59}",
          "§e58. {name_58} §7- §e{stats_58}", "§e57. {name_57} §7- §e{stats_57}",
          "§e56. {name_56} §7- §e{stats_56}", "§e55. {name_55} §7- §e{stats_55}",
          "§e54. {name_54} §7- §e{stats_54}", "§e53. {name_53} §7- §e{stats_53}",
          "§e52. {name_52} §7- §e{stats_52}", "§e51. {name_51} §7- §e{stats_51}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(6/10)");
  
  public static List<String> lobby$leaderboard$kills$page7 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e70. {name_70} §7- §e{stats_70}", "§e69. {name_69} §7- §e{stats_69}",
          "§e68. {name_68} §7- §e{stats_68}", "§e67. {name_67} §7- §e{stats_67}",
          "§e66. {name_66} §7- §e{stats_66}", "§e65. {name_65} §7- §e{stats_65}",
          "§e64. {name_64} §7- §e{stats_64}", "§e63. {name_63} §7- §e{stats_63}",
          "§e62. {name_62} §7- §e{stats_62}", "§e61. {name_61} §7- §e{stats_61}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(7/10)");
  
  public static List<String> lobby$leaderboard$kills$page8 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e80. {name_80} §7- §e{stats_80}", "§e79. {name_79} §7- §e{stats_79}",
          "§e78. {name_78} §7- §e{stats_78}", "§e77. {name_77} §7- §e{stats_77}",
          "§e76. {name_76} §7- §e{stats_76}", "§e75. {name_75} §7- §e{stats_75}",
          "§e74. {name_74} §7- §e{stats_74}", "§e73. {name_73} §7- §e{stats_73}",
          "§e72. {name_72} §7- §e{stats_72}", "§e71. {name_71} §7- §e{stats_71}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(8/10)");
  
  public static List<String> lobby$leaderboard$kills$page9 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e90. {name_90} §7- §e{stats_90}", "§e89. {name_89} §7- §e{stats_89}",
          "§e88. {name_88} §7- §e{stats_88}", "§e87. {name_87} §7- §e{stats_87}",
          "§e86. {name_86} §7- §e{stats_86}", "§e85. {name_85} §7- §e{stats_85}",
          "§e84. {name_84} §7- §e{stats_84}", "§e83. {name_83} §7- §e{stats_83}",
          "§e82. {name_82} §7- §e{stats_82}", "§e81. {name_81} §7- §e{stats_81}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(9/10)");
  
  public static List<String> lobby$leaderboard$kills$page10 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e100. {name_100} §7- §e{stats_100}", "§e99. {name_99} §7- §e{stats_99}",
          "§e98. {name_98} §7- §e{stats_98}", "§e97. {name_97} §7- §e{stats_97}",
          "§e96. {name_96} §7- §e{stats_96}", "§e95. {name_95} §7- §e{stats_95}",
          "§e94. {name_94} §7- §e{stats_94}", "§e93. {name_93} §7- §e{stats_93}",
          "§e92. {name_92} §7- §e{stats_92}", "§e91. {name_91} §7- §e{stats_91}", "",
          "§e§lABATES FINAIS", "§b§lTOP 100 §8(10/10)");
  public static List<String> lobby$leaderboard$beds$hologram = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8({page_info})");
  
  // Páginas separadas para camas
  public static List<String> lobby$leaderboard$beds$page1 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e10. {name_10} §7- §e{stats_10}", "§e9. {name_9} §7- §e{stats_9}",
          "§e8. {name_8} §7- §e{stats_8}", "§e7. {name_7} §7- §e{stats_7}",
          "§e6. {name_6} §7- §e{stats_6}", "§e5. {name_5} §7- §e{stats_5}",
          "§e4. {name_4} §7- §e{stats_4}", "§e3. {name_3} §7- §e{stats_3}",
          "§e2. {name_2} §7- §e{stats_2}", "§e1. {name_1} §7- §e{stats_1}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(1/10)");
  
  public static List<String> lobby$leaderboard$beds$page2 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e20. {name_20} §7- §e{stats_20}", "§e19. {name_19} §7- §e{stats_19}",
          "§e18. {name_18} §7- §e{stats_18}", "§e17. {name_17} §7- §e{stats_17}",
          "§e16. {name_16} §7- §e{stats_16}", "§e15. {name_15} §7- §e{stats_15}",
          "§e14. {name_14} §7- §e{stats_14}", "§e13. {name_13} §7- §e{stats_13}",
          "§e12. {name_12} §7- §e{stats_12}", "§e11. {name_11} §7- §e{stats_11}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(2/10)");
  
  public static List<String> lobby$leaderboard$beds$page3 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e30. {name_30} §7- §e{stats_30}", "§e29. {name_29} §7- §e{stats_29}",
          "§e28. {name_28} §7- §e{stats_28}", "§e27. {name_27} §7- §e{stats_27}",
          "§e26. {name_26} §7- §e{stats_26}", "§e25. {name_25} §7- §e{stats_25}",
          "§e24. {name_24} §7- §e{stats_24}", "§e23. {name_23} §7- §e{stats_23}",
          "§e22. {name_22} §7- §e{stats_22}", "§e21. {name_21} §7- §e{stats_21}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(3/10)");
  
  public static List<String> lobby$leaderboard$beds$page4 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e40. {name_40} §7- §e{stats_40}", "§e39. {name_39} §7- §e{stats_39}",
          "§e38. {name_38} §7- §e{stats_38}", "§e37. {name_37} §7- §e{stats_37}",
          "§e36. {name_36} §7- §e{stats_36}", "§e35. {name_35} §7- §e{stats_35}",
          "§e34. {name_34} §7- §e{stats_34}", "§e33. {name_33} §7- §e{stats_33}",
          "§e32. {name_32} §7- §e{stats_32}", "§e31. {name_31} §7- §e{stats_31}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(4/10)");
  
  public static List<String> lobby$leaderboard$beds$page5 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e50. {name_50} §7- §e{stats_50}", "§e49. {name_49} §7- §e{stats_49}",
          "§e48. {name_48} §7- §e{stats_48}", "§e47. {name_47} §7- §e{stats_47}",
          "§e46. {name_46} §7- §e{stats_46}", "§e45. {name_45} §7- §e{stats_45}",
          "§e44. {name_44} §7- §e{stats_44}", "§e43. {name_43} §7- §e{stats_43}",
          "§e42. {name_42} §7- §e{stats_42}", "§e41. {name_41} §7- §e{stats_41}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(5/10)");
  
  public static List<String> lobby$leaderboard$beds$page6 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e60. {name_60} §7- §e{stats_60}", "§e59. {name_59} §7- §e{stats_59}",
          "§e58. {name_58} §7- §e{stats_58}", "§e57. {name_57} §7- §e{stats_57}",
          "§e56. {name_56} §7- §e{stats_56}", "§e55. {name_55} §7- §e{stats_55}",
          "§e54. {name_54} §7- §e{stats_54}", "§e53. {name_53} §7- §e{stats_53}",
          "§e52. {name_52} §7- §e{stats_52}", "§e51. {name_51} §7- §e{stats_51}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(6/10)");
  
  public static List<String> lobby$leaderboard$beds$page7 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e70. {name_70} §7- §e{stats_70}", "§e69. {name_69} §7- §e{stats_69}",
          "§e68. {name_68} §7- §e{stats_68}", "§e67. {name_67} §7- §e{stats_67}",
          "§e66. {name_66} §7- §e{stats_66}", "§e65. {name_65} §7- §e{stats_65}",
          "§e64. {name_64} §7- §e{stats_64}", "§e63. {name_63} §7- §e{stats_63}",
          "§e62. {name_62} §7- §e{stats_62}", "§e61. {name_61} §7- §e{stats_61}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(7/10)");
  
  public static List<String> lobby$leaderboard$beds$page8 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e80. {name_80} §7- §e{stats_80}", "§e79. {name_79} §7- §e{stats_79}",
          "§e78. {name_78} §7- §e{stats_78}", "§e77. {name_77} §7- §e{stats_77}",
          "§e76. {name_76} §7- §e{stats_76}", "§e75. {name_75} §7- §e{stats_75}",
          "§e74. {name_74} §7- §e{stats_74}", "§e73. {name_73} §7- §e{stats_73}",
          "§e72. {name_72} §7- §e{stats_72}", "§e71. {name_71} §7- §e{stats_71}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(8/10)");
  
  public static List<String> lobby$leaderboard$beds$page9 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e90. {name_90} §7- §e{stats_90}", "§e89. {name_89} §7- §e{stats_89}",
          "§e88. {name_88} §7- §e{stats_88}", "§e87. {name_87} §7- §e{stats_87}",
          "§e86. {name_86} §7- §e{stats_86}", "§e85. {name_85} §7- §e{stats_85}",
          "§e84. {name_84} §7- §e{stats_84}", "§e83. {name_83} §7- §e{stats_83}",
          "§e82. {name_82} §7- §e{stats_82}", "§e81. {name_81} §7- §e{stats_81}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(9/10)");
  
  public static List<String> lobby$leaderboard$beds$page10 = Arrays
      .asList("{weekly_color}Semanal {monthly_color}Mensal {total_color}Total", "§6§lClique para trocar!",
          "§e100. {name_100} §7- §e{stats_100}", "§e99. {name_99} §7- §e{stats_99}",
          "§e98. {name_98} §7- §e{stats_98}", "§e97. {name_97} §7- §e{stats_97}",
          "§e96. {name_96} §7- §e{stats_96}", "§e95. {name_95} §7- §e{stats_95}",
          "§e94. {name_94} §7- §e{stats_94}", "§e93. {name_93} §7- §e{stats_93}",
          "§e92. {name_92} §7- §e{stats_92}", "§e91. {name_91} §7- §e{stats_91}", "",
          "§e§lCAMAS DESTRUÍDAS", "§b§lTOP 100 §8(10/10)");
  public static List<String> lobby$leaderboard$modos$hologram = Arrays
      .asList("§eClique para alterar!",
          "",
          "{quartetos_color}Quartetos",
          "{duplas_color}Duplas",
          "{solo_color}Solo",
          "{geral_color}Geral",
          "§b§lSELECIONAR MODO");
  
  public static String lobby$npc$play$connect = "§aConectando...";
  public static String lobby$npc$play$menu$info$item = "PAPER : 1 : nome>§aInformações : desc>{desc}";
  public static String lobby$npc$play$menu$info$desc_limit =
      "§fLimite Diário: §7{limit}\n \n§7Jogadores que possuem o grupo §aVIP §7ou\n§7superior, podem escolher o mapa sem\n§7limite algum.\n \n&7www.redeslick.com/loja";
  public static String lobby$npc$play$menu$info$desc_not_limit = "§7Você não possui limite diário de seleções.";
  
  public static List<String> lobby$npc$stats$hologram = Arrays
      .asList("§6Estatísticas", "Total de Eliminações: §7%Core_BedWars_kills%",
          "Total de Vitórias: §7%Core_BedWars_wins%", "§e§lCLIQUE DIREITO");
  public static List<String> lobby$npc$play$dupla$hologram = Arrays
      .asList("§bDuplas", "§a{players} Jogadores");
  public static List<String> lobby$npc$play$solo$hologram = Arrays
      .asList("§bSolo", "§a{players} Jogadores");
  public static List<String> lobby$npc$play$quarteto$hologram = Arrays
      .asList("§bQuartetos", "§a{players} Jogadores");

  public static String lobby$npc$play$dupla$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$dupla$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
  public static String lobby$npc$play$solo$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$solo$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
  public static String lobby$npc$play$quarteto$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$quarteto$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
 
  // Cosméticos
  public static String cosmetics$color$locked = "§a";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§6";
  
  public static String cosmetics$icon$perm_desc$common = "§cVocê não possui permissão.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusivo para {rank} §7ou superior.";
  public static String cosmetics$icon$buy_desc$enough = "§cVocê não possui saldo suficiente.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClique para comprar!";
  public static String cosmetics$icon$has_desc$select = "§eClique para selecionar!";
  public static String cosmetics$icon$has_desc$selected = "§eClique para deselecionar!";
  
  public static String cosmetics$kill_effect$icon$perm_desc$start = "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$kill_effect$icon$buy_desc$start =
      "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$kill_effect$icon$has_desc$start = "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$shopkeeperskin$icon$perm_desc$start =
      "§7Altera a skin da Loja para {name}\n&7durante a partida.\n  \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$shopkeeperskin$icon$buy_desc$start =
      "§7Altera a skin da Loja para {name}\n&7durante a partida.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$shopkeeperskin$icon$has_desc$start =
      "§7Altera a skin da Loja para {name}\n&7durante a partida.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
 
  public static String cosmetics$break_effect$icon$perm_desc$start =
      "§7Quando você quebrar uma cama\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$break_effect$icon$buy_desc$start =
      "§7Quando você quebrar uma cama\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$break_effect$icon$has_desc$start =
      "§7Quando você quebrar uma cama\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$deathcry$icon$perm_desc$start =
      "§7Quando você morrer tocará\n§7o grito de morte {name}.\n \n§6Clique direito para escutar!\n  \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$deathcry$icon$buy_desc$start =
      "§7Quando você morrer tocará\n§7o grito de morte {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n§6Clique direito para escutar!\n \n{buy_desc_status}";
  public static String cosmetics$deathcry$icon$has_desc$start =
      "§7Quando você morrer tocará\n§7o grito de morte {name}.\n \n§6Clique direito para escutar!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$projectile_effect$icon$perm_desc$start =
      "§7Quando você jogar um projétil\n§7sairá partículas de {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$projectile_effect$icon$buy_desc$start =
      "§7Quando você jogar um projétil\n§7sairá partículas de {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$projectile_effect$icon$has_desc$start =
      "§7Quando você jogar um projétil\n§7sairá partículas de {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
 
  public static String cosmetics$fall_effect$icon$perm_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$fall_effect$icon$buy_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$fall_effect$icon$has_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$teleport_effect$icon$perm_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$teleport_effect$icon$buy_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$teleport_effect$icon$has_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$wood_types$icon$perm_desc$start =
      "§7Costumizar a madeira do \n§7Vendedor para {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$wood_types$icon$buy_desc$start =
      "§7Costumizar a madeira do \n§7Vendedor para {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$wood_types$icon$has_desc$start =
      "§7Costumizar a madeira do \n§7Vendedor para {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  
  public static String cosmetics$death_message$icon$perm_desc$start =
      "\n§6Clique direito para ver!\n \n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$death_message$icon$buy_desc$start =
      "\n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$death_message$icon$has_desc$start =
      "\n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
 
  public static String cosmetics$cage$icon$perm_desc$start =
      "§7Altere sua jaula para {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$cage$icon$buy_desc$start =
      "§7Altere sua jaula para {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins\n \n{buy_desc_status}";
  public static String cosmetics$cage$icon$has_desc$start =
      "§7Altere sua jaula para {name}.\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";


 
  public static String cosmetics$win_animation$icon$perm_desc$start = "\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$win_animation$icon$buy_desc$start = "\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$win_animation$icon$has_desc$start = "\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
 
  // In-game
  public static List<String> ingame$generators$hologram = Arrays
      .asList("§eNível §c{tier}", "{type}", "§eGera em §c{time} §esegundo{s}");
  public static List<String> ingame$npc$shop$item$hologram = Arrays
      .asList("§e§lLOJA DE", "§e§lITENS", "§b§lCLIQUE DIREITO");
  public static List<String> ingame$npc$shop$upgrade$hologram = Arrays
      .asList("§e§lLOJA DE", "§e§lMELHORIAS", "§b§lCLIQUE DIREITO");
  
  public static String ingame$broadcast$join = "{player} §eentrou na partida! §a({players}/{max_players})";
  public static String ingame$broadcast$leave = "{player} §csaiu da partida! §a({players}/{max_players})";
  public static String ingame$broadcast$starting = "§aO jogo começa em §f{time} §asegundo{s}.";
  public static String ingame$broadcast$suicide = "{name} §amorreu sozinho.";
  public static String ingame$broadcast$killed = "{name} §afoi abatido por {killer}";
  public static String ingame$broadcast$generator_upgrade$diamond = "§eOs geradores de §b§lDiamante §eforam aprimorados para o nivel §b§l{tier}§e!";
  public static String ingame$broadcast$generator_upgrade$emerald = "§eOs geradores de §2§lEsmeralda §eforam aprimorados para o nivel §b§l{tier}§e!";
  public static String ingame$broadcast$team_eliminated = "\n §f§lTIME ELIMINADO > §cO time {team} §cfoi eliminado!\n ";
  public static String ingame$broadcast$bed_destroyself = "\n §f§lCAMA DESTRUIDA > §7A sua cama foi destruída por {name}\n ";
  public static String ingame$broadcast$bed_destroy = "\n §f§lCAMA DESTRUIDA > §7A cama do time {team}§7 foi destruída por {name}\n ";
  public static String ingame$broadcast$double_kill = "§a. §e§lDOUBLE KILL";
  public static String ingame$broadcast$triple_kill = "§a. §b§lTRIPLE KILL";
  public static String ingame$broadcast$quadra_kill = "§a. §6§lQUADRA KILL";
  public static String ingame$broadcast$monster_kill = "§a. §c§lMONSTER KILL";
  public static String ingame$broadcast$end = " \n§aO tempo acabou, não houve ganhadores.\n ";
  public static String ingame$broadcast$win$solo = " \n{name} §avenceu a partida!\n ";
  public static String ingame$broadcast$win$dupla = " \n{name} §avenceram a partida!\n ";
  public static String ingame$titles$death$header = "§c§lVOCE MORREU";
  public static String ingame$titles$death$footer = "§7Você agora é um espectador";
  public static String ingame$titles$win$header = "§a§lVITÓRIA";
  public static String ingame$titles$win$footer = "§7Seu time venceu!";
  public static String ingame$titles$beddestroy_self$header = "§c§lCAMA DESTRUIDA";
  public static String ingame$titles$beddestroy_self$footer = "§7Você não renascerá mais.";
  public static String ingame$titles$lose$header = "§c§lFIM DE JOGO";
  public static String ingame$titles$lose$footer = "§7Você não foi vitorioso dessa vez";
  public static String ingame$messages$bow$hit = "{name} §aestá com §c{hp} §ade HP.";
  public static String ingame$messages$coins$base = " \n  §a{coins} coins ganhos nesta partida:\n {coins_win}{coins_beds}{coins_kills}\n ";
  public static String ingame$messages$coins$win = "\n       §a+{coins} §fpor vencer o jogo";
  public static String ingame$messages$coins$beds = "\n       §a+{coins} §fpor destruir §c{beds} §fcama{s}";
  public static String ingame$messages$coins$kills = "\n       §a+{coins} §fpor realizar §c{kills} §fabate{s}";
  
  /**
   * Inicializa o sistema de linguagem interno
   * Agora todas as mensagens são carregadas diretamente das constantes da classe
   */
  public static void setupLanguage() {
    // Aplicar formatação de cores em todas as strings
    applyColorFormatting();
    
    Main.getInstance().getLogger().info("Sistema de linguagem interno carregado com sucesso.");
  }
  
  /**
   * Aplica formatação de cores em todas as strings da classe
   */
  private static void applyColorFormatting() {
    try {
      java.lang.reflect.Field[] fields = Language.class.getDeclaredFields();
      for (java.lang.reflect.Field field : fields) {
        if (field.getName().contains("$") && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
          field.setAccessible(true);
          Object value = field.get(null);
          
          if (value instanceof String) {
            String stringValue = (String) value;
            field.set(null, StringUtils.formatColors(stringValue).replace("\\n", "\n"));
          } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            List<Object> newList = new java.util.ArrayList<>(list.size());
            for (Object item : list) {
              if (item instanceof String) {
                newList.add(StringUtils.formatColors((String) item).replace("\\n", "\n"));
              } else {
                newList.add(item);
              }
            }
            field.set(null, newList);
          }
        }
      }
    } catch (Exception e) {
      Main.getInstance().getLogger().warning("Erro ao aplicar formatação de cores: " + e.getMessage());
    }
  }
}
