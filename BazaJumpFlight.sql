PGDMP  
                     |        
   JumpFlight    16.3    16.3     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16572 
   JumpFlight    DATABASE        CREATE DATABASE "JumpFlight" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE "JumpFlight";
                postgres    false            �            1259    16573    loty    TABLE     �   CREATE TABLE public.loty (
    id integer NOT NULL,
    wylot character varying(50),
    przylot character varying(50),
    data date,
    czas time without time zone,
    wolne_miejsca integer,
    cena real
);
    DROP TABLE public.loty;
       public         heap    postgres    false            �            1259    16600    loty_id_seq    SEQUENCE     �   ALTER TABLE public.loty ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.loty_id_seq
    START WITH 11
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    215            �            1259    16576    pasazerowie    TABLE     �   CREATE TABLE public.pasazerowie (
    id integer NOT NULL,
    imie character varying(50),
    nazwisko character varying(50),
    email character varying(50)
);
    DROP TABLE public.pasazerowie;
       public         heap    postgres    false            �            1259    16601    pasazerowie_id_seq    SEQUENCE     �   ALTER TABLE public.pasazerowie ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pasazerowie_id_seq
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            �            1259    16579 
   rezerwacja    TABLE     �   CREATE TABLE public.rezerwacja (
    id integer NOT NULL,
    loty_id integer,
    pasazerowie_id integer,
    kod_rezerwacji integer
);
    DROP TABLE public.rezerwacja;
       public         heap    postgres    false            �            1259    16603    rezerwacja_id_seq    SEQUENCE     �   ALTER TABLE public.rezerwacja ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.rezerwacja_id_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    217            �          0    16573    loty 
   TABLE DATA           S   COPY public.loty (id, wylot, przylot, data, czas, wolne_miejsca, cena) FROM stdin;
    public          postgres    false    215   �       �          0    16576    pasazerowie 
   TABLE DATA           @   COPY public.pasazerowie (id, imie, nazwisko, email) FROM stdin;
    public          postgres    false    216   ~       �          0    16579 
   rezerwacja 
   TABLE DATA           Q   COPY public.rezerwacja (id, loty_id, pasazerowie_id, kod_rezerwacji) FROM stdin;
    public          postgres    false    217   0       �           0    0    loty_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.loty_id_seq', 11, false);
          public          postgres    false    218            �           0    0    pasazerowie_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.pasazerowie_id_seq', 8, false);
          public          postgres    false    219            �           0    0    rezerwacja_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.rezerwacja_id_seq', 7, true);
          public          postgres    false    220            %           2606    16583    loty loty_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.loty
    ADD CONSTRAINT loty_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.loty DROP CONSTRAINT loty_pkey;
       public            postgres    false    215            '           2606    16585    pasazerowie pasazerowie_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.pasazerowie
    ADD CONSTRAINT pasazerowie_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.pasazerowie DROP CONSTRAINT pasazerowie_pkey;
       public            postgres    false    216            )           2606    16599 (   rezerwacja rezerwacja_kod_rezerwacji_key 
   CONSTRAINT     m   ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT rezerwacja_kod_rezerwacji_key UNIQUE (kod_rezerwacji);
 R   ALTER TABLE ONLY public.rezerwacja DROP CONSTRAINT rezerwacja_kod_rezerwacji_key;
       public            postgres    false    217            +           2606    16587    rezerwacja rezerwacja_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT rezerwacja_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.rezerwacja DROP CONSTRAINT rezerwacja_pkey;
       public            postgres    false    217            ,           2606    16588 "   rezerwacja rezerwacja_loty_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT rezerwacja_loty_id_fkey FOREIGN KEY (loty_id) REFERENCES public.loty(id);
 L   ALTER TABLE ONLY public.rezerwacja DROP CONSTRAINT rezerwacja_loty_id_fkey;
       public          postgres    false    217    215    4645            -           2606    16593 )   rezerwacja rezerwacja_pasazerowie_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rezerwacja
    ADD CONSTRAINT rezerwacja_pasazerowie_id_fkey FOREIGN KEY (pasazerowie_id) REFERENCES public.pasazerowie(id);
 S   ALTER TABLE ONLY public.rezerwacja DROP CONSTRAINT rezerwacja_pasazerowie_id_fkey;
       public          postgres    false    217    216    4647            �   �   x�UϻN�0�z�_�f��w :!���x�)",[����Ǳ����{���g��p�5E��jH7�VNiGl��(N8��K@�	�����Ϲ�sz�K?�a�m#!'�b�?M�v��o�t_�iX��kM`4jx��"���I['k��Zl7��C��-��N���f�~��q:�eyv��,5:B{����L]�w5}YG؝������ZUl�f�[\���y�/���'D�~-e�      �   �   x�E��
�0�����x���"Tp�f��������MR%���ìP�ը���V�Q�-+C�[�F��9F�c���#r����T�Ϛqa:��^�9���-�3�yj��!�����P��4���&tq�wN�(�|��ٛ��M$�0�T<�Q���-��q��$��А`       �   ?   x����@�w�1s� _�,���j%ŐQ*�&yl5fq����Z>�t����7��%���
m     