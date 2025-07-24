package com.example.egerdon;

public class enums {
    public enum PropertyType { ONE_ROOM, VILLA, OFFICETEL, APARTMENT, OTHER }  // 매물 유형

    public enum LayoutType { OPEN, SEPARATED, DUPLEX }  // 구조 유형

    public enum ContractType { MONTHLY, JEONSE, HALF_JEONSE }  // 거래 유형

    public enum ContractStatus { AVAILABLE, RESERVED, COMPLETED }  // 계약 상태

    public enum EntranceType { SHARED, CORRIDOR, STAIR, PRIVATE }  // 현관 유형

    public enum Orientation { SOUTH, NORTH, EAST, WEST, SOUTHEAST, SOUTHWEST }  // 동향

    public enum HeatingType { INDIVIDUAL, CENTRAL, AIR_CONDITIONER, COMBINED }  // 냉난방 방식

    public enum SoundproofLevel { EXCELLENT, NORMAL, POOR }  // 방음 수준

    public enum ListerType { OWNER, TENANT, MANAGER, AGENT }  // 등록자 유형

}
