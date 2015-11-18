/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package py.edu.uca.intercajas.client.menumail;

import java.util.ArrayList;

/**
 * A simple client-side generator of fake email messages.
 */
public class MailItems {

  private static final int NUM_ITEMS = 64;
  private static final int FRAGMENTS_PER_EMAIL = 10;

  private static final String[] senders = new String[] {
      "FISCAL", "ANDE", "ITAUPU",
      "BANCARIA", "FERRICARRIL", "MINICIPAL", "PARLAMENTARIA"};

  private static final String[] emails = new String[] {
      "Solicitud", "Antiguedad", "Liquidacion",
      "Aclaracion", "Finiquito"};

  private static final String[] subjects = new String[] {
      "EXP 203/2015 Juan Perez, En tramite",
      "EXP 323/2015 Pedro Cano, Proseguir tramite",
      "EXP 123/2015 Carlos Cabral, para liquidacion",
      "EXP 327/2015 Juan Carlos Frutos, para liquidacion",
      "EXP 127/2015 Lourdes Drews, para liquidacion"};

  private static final String[] fragments = new String[] {
      "Señores Caja XXXX<br>",
      "Tengo el agrado de dirigirme a ustedes, a fin de solicitar proseguir tramite ",
      "I have all necessary legal documents that can be used to back up any "
          + "claim we may make. All I require is your honest Co-operation, "
          + "Confidentiality and A trust to enable us sees this transaction through. "
          + "I guarantee you that this will be executed under a legitimate "
          + "arrangement that will protect you from any breach of the law. Please "
          + "get in touch with me urgently by E-mail and "
          + "Provide me with the following;<br>",
      "The OIL sector is going crazy. This is our weekly gift to you!<br>"
          + "<br>" + "Get KKPT First Thing, This Is Going To Run!<br>" + "<br>"
          + "Check out Latest NEWS!<br>" + "<br>"
          + "KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>"
          + "Our last pick gained $2.16 in 4 days of trading.<br>",
      "LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. "
          + "(Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that "
          + "its operator for the Corsicana Field, JMT Resources, Ltd. (\"JMT\") "
          + "will commence a re-work program on its Pecan Gap wells in the next week. "
          + "The re-work program will consist of drilling six lateral bore production "
          + "strings from the existing well bore. This process, known as Radial Jet "
          + "Enhancement, will utilize high pressure fluids to drill the lateral well "
          + "bores, which will extend out approximately 350\' each.",
      "JMT has contracted with Well Enhancement Services, LLC (www."
          + "wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. "
          + "A small sand frac will follow the drilling of the lateral well bores in "
          + "order to enhance permeability and create larger access to the Pecan Gap "
          + "reservoir. Total cost of the re-work per well is estimated to be "
          + "approximately $50,000 USD.",
      "Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de "
          + "R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - "
          + "Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), "
          + "Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, "
          + "fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o "
          + "arquivo previamente salvo, imprima uma folha e salve a c?pia em seu "
          + "computador para evitar transtornos decorrentes da perda do mesmo. "
          + "Lembramos que o Vale-Presente ? ?nico e intransfer?vel.",
      "Large Marketing Campaign running this weekend!<br>" + "<br>"
          + "Should you get in today before it explodes?<br>" + "<br>"
          + "This Will Fly Starting Monday!",
      "PREMIER INFORMATION (PIFR)<br>"
          + "A U.S. based company offers specialized information management "
          + "serices to both the Insurance and Healthcare Industries. The services "
          + "we provide are specific to each industry and designed for quick "
          + "response and maximum security.<br>" + "<br>" + "STK- PIFR<br>"
          + "Current Price: .20<br>"
          + "This one went to $2.80 during the last marketing Campaign!",
      "These partnerships specifically allow Premier to obtain personal health "
          + "information, as governed by the Health In-surancee Portability and "
          + "Accountability Act of 1996 (HIPAA), and other applicable state laws and "
          + "regulations.<br><br>"
          + "Global HealthCare Market Undergoing Digital Conversion",
      ">>   Componentes e decodificadores; confira aqui;<br>"
          + " http://br.geocities.com/listajohn/index.htm<br>",
      "THE GOVERNING AWARD<br>" + "NETHERLANDS HEAD OFFICE<br>"
          + "AC 76892 HAUITSOP<br>" + "AMSTERDAM, THE NETHERLANDS.<br>"
          + "FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>"
          + "INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>"
          + "REF NUMBER: 14235/089.<br>" + "BATCH NUMBER: 304/64780/IFY.<br>"
          + "RE/AWARD NOTIFICATION<br>",
      "We are pleased to inform you of the announcement today 13th of April "
          + "2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on "
          + "the 28th of March 2006. The THREE Winning Addresses were randomly "
          + "selected from a batch of 10,000,000 international email addresses. "
          + "Your email address emerged alongside TWO others as a category B winner "
          + "in this year\'s Annual GOVERNING AWARD Draw.<br>",
      ">> obrigado por me dar esta pequena aten??o !!!<br>"
          + "CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS "
          + "LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES "
          + "DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , "
          + "TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E "
          + "ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , "
          + "DOCUMENT?RIOS ,SHOWS , ETC,<br><br>"
          + "CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>"
          + "DECODERS NET TVA DESBLOQUEADOS                       390,00<br>"
          + "KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>"
          + "TECSAT FREE   450,00<br>"
          + "TENHO TB ACESS?RIOS , CABOS, LNB .<br>",
      "********************************************************************<br>"
          + " Original filename: mail.zip<br>"
          + " Virus discovered: JS.Feebs.AC<br>"
          + "********************************************************************<br>"
          + " A file that was attached to this email contained a virus.<br>"
          + " It is very likely that the original message was generated<br>"
          + " by the virus and not a person - treat this message as you would<br>"
          + " any other junk mail (spam).<br>"
          + " For more information on why you received this message please visit:<br>",
      "Put a few letters after your name. Let us show you how you can do it in "
          + "just a few days.<br><br>"
          + "http://thewrongchoiceforyou.info<br><br>"
          + "kill future mailing by pressing this : see main website",
      "We possess scores of pharmaceutical products handy<br>"
          + "All med\'s are made in U.S. laboratories<br>"
          + "For your wellbeing! Very rapid, protected and secure<br>"
          + "Ordering, No script required. We have the pain aid you require<br>",
      "\"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, "
          + "but Austria never has wished, and does not wish, for war. She is "
          + "betraying us! Russia alone must save Europe. Our gracious sovereign "
          + "recognizes his high vocation and will be true to it. That is the one "
          + "thing I have faith in! Our good and wonderful sovereign has to perform "
          + "the noblest role on earth, and he is so virtuous and noble that God "
          + "will not forsake him. He will fulfill his vocation and crush the hydra "
          + "of revolution, which has become more terrible than ever in the person of "
          + "this murderer and villain! We alone must avenge the blood of the "
          + "just one.... Whom, I ask you, can we rely on?... England with "
          + "her commercial spirit will not and cannot understand the Emperor "
          + "Alexander\'s loftiness of soul. She has refused to evacuate Malta. "
          + "She wanted to find, and still seeks, some secret motive in our "
          + "actions. What answer did Novosiltsev get? None. The English have not "
          + "understood and cannot understand the self-ab!<br>negation of our "
          + "Emperor who wants nothing for himself, but only desires the good "
          + "of mankind. And what have they promised? Nothing! And what little "
          + "they have promised they will not perform! Prussia has always "
          + "declared that Buonaparte is invincible, and that all Europe is "
          + "powerless before him.... And I don\'t believe a word that Hardenburg "
          + "says, or Haugwitz either. This famous Prussian neutrality is just a "
          + "trap. I have faith only in God and the lofty destiny of our adored "
          + "monarch. He will save Europe!\"<br>\"Those were extremes, no doubt, "
          + "but they are not what is most important. What is important are the "
          + "rights of man, emancipation from prejudices, and equality of "
          + "citizenship, and all these ideas Napoleon has retained in full "
          + "force.\""};

  private static int senderIdx = 0, emailIdx = 0, subjectIdx = 0,
      fragmentIdx = 0;
  private static ArrayList<MailItem> items = new ArrayList<MailItem>();

  static {
    for (int i = 0; i < NUM_ITEMS; ++i) {
      items.add(createFakeMail());
    }
  }

  public static MailItem getMailItem(int index) {
    if (index >= items.size()) {
      return null;
    }
    return items.get(index);
  }

  public static int getMailItemCount() {
    return items.size();
  }

  private static MailItem createFakeMail() {
    String sender = senders[senderIdx++];
    if (senderIdx == senders.length) {
      senderIdx = 0;
    }

    String email = emails[emailIdx++];
    if (emailIdx == emails.length) {
      emailIdx = 0;
    }

    String subject = subjects[subjectIdx++];
    if (subjectIdx == subjects.length) {
      subjectIdx = 0;
    }

    String body = "";
    for (int i = 0; i < FRAGMENTS_PER_EMAIL; ++i) {
      body += fragments[fragmentIdx++];
      if (fragmentIdx == fragments.length) {
        fragmentIdx = 0;
      }
    }

    return new MailItem(sender, email, subject, body);
  }
}
