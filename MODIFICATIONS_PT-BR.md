# PIXqsl – MultiGrid modification

Alterações realizadas:

- Estações agora podem usar 1, 2 ou 4 grids.
- Com 1 grid, o campo aceita locators Maidenhead de 4 ou 6 caracteres.
- Com 2 ou 4 grids, cada campo aceita exatamente 4 caracteres.
- Para 2/4 grids, o aplicativo valida que os grids são adjacentes e formam uma borda/interseção válida para VUCC.
- Estações existentes com um único grid continuam compatíveis.
- O GABBI/TQ8 passa a representar 2/4 grids no campo `GRIDSQUARE` como uma lista separada por vírgulas, conforme o funcionamento documentado do LoTW/TQSL.
- A verificação de consistência de `MY_GRIDSQUARE`/`MY_VUCC_GRIDS` foi ajustada para reconhecer estações com múltiplos grids.
- O interpretador da resposta do upload LoTW foi tornado mais robusto, inclusive para mensagens multilinha e respostas HTTP de erro.
- `versionCode` foi aumentado para `20260731` e `versionName` para `2026-SpringBlossom-b-MultiGrid`.

## Observação sobre compilação

O código foi revisado e as alterações foram feitas diretamente sobre o projeto enviado. Não foi possível executar o build Gradle neste ambiente porque o Gradle Wrapper tentou baixar o Gradle 9.3.1 da internet e o ambiente de execução não possui acesso de rede.

Recomenda-se abrir o projeto no Android Studio e executar `assembleDebug` antes de instalar no aparelho.

\n## Auditoria adicional do TQ8/TQSL\n\n- Corrigida a seção inicial para usar `TQSL_IDENT`, em vez de um registro ADIF `Ident`.\n- O certificado no `tCERT` agora é emitido como base64 do DER sem quebras de linha, com comprimento ADIF correspondente.\n- A emissão de campos `tSTATION` passou a seguir a ordem determinística da sigspec.\n- Antes de gerar o TQ8, o app valida a janela de validade do certificado, `STATION_CALLSIGN`/`OPERATOR` quando presentes e a combinação `PROP_MODE=SAT`/`SAT_NAME`.\n- Após cada assinatura RSA, o próprio aplicativo verifica a assinatura usando a chave pública do certificado; um TQ8 não é salvo se a assinatura não puder ser verificada localmente.\n- A implementação continua usando RSA/SHA-1 para a assinatura do registro, compatível com a estrutura LoTW/TrustedQSL usada pelo projeto. Não foi introduzido um algoritmo novo sem evidência no protocolo oficial.\n